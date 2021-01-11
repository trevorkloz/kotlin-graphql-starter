package com.example.backend.service

import com.example.backend.domain.PersonEntity
import com.example.backend.graphql.Person
import com.example.backend.graphql.Search
import com.example.backend.graphql.SortBy
import com.google.common.primitives.Bytes
import org.hibernate.transform.Transformers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

@Component
class PersonSearchService {

    @Autowired
    lateinit var loadingService: LoadingService

    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun find(input: Search): Pair<List<Person?>, Int> {

       val persons = mutableListOf<Person>()
       val searchResult = findPersonEntity(input)
       searchResult.first.forEach {
            it?.let {
                persons.add(loadingService.buildPerson(it))
            }
       }
       return Pair(persons, searchResult.second)
    }

    private fun wherePerson(input: Search, predicates: MutableList<String>) {

        input.firstname?.let {
            predicates.add("""trim(lower(p.firstname)) = '${it.toLowerCase()}'""")
        }

        input.lastname?.let {
            predicates.add("""trim(lower(p.lastname)) = '${it.toLowerCase()}'""")
        }
    }

    private fun whereAddress(input: Search, predicates: MutableList<String>, joins: MutableMap<String, String>) {

        joins["a"] = "LEFT OUTER JOIN address a ON p.address_id=a.id"

        input.street?.let {
            val subpredicates = mutableListOf<String>()
            joins.forEach { join ->
                subpredicates.add("""trim(lower(${join.key}.street)) LIKE '%${it.toLowerCase()}%'""")
            }
            predicates.add(subpredicates.joinToString(" OR "))
        }
    }

    private fun findPersonEntity(input: Search): Pair<List<PersonEntity?>, Int> {

        val predicates = mutableListOf<String>()
        val joins = mutableMapOf<String, String>()

        wherePerson(input, predicates)
        whereAddress(input, predicates, joins)

        val predicateStr = if (predicates.isNotEmpty()) {
            predicates.joinToString(") AND (", "WHERE (", ")")
        } else ""
        val joinsStr = if (joins.isNotEmpty()) {
            joins.values.joinToString(" ")
        } else ""

        val sortDir= if (input._sortDirection != null) {
            input._sortDirection?.name
        } else {
            "ASC"
        }

        val orderList = mutableListOf<String>()
        if (input._sortBy == null) {
            orderList.add("p.lastname $sortDir")
        } else {
            when (input._sortBy) {
                SortBy.FIRSTNAME -> orderList.add("p.firstname $sortDir")
                SortBy.LASTNAME -> orderList.add("p.lastname $sortDir")
            }
        }
        val orderStr = "ORDER BY " + orderList.joinToString()

        val q: Query = entityManager.createNativeQuery(
                """
                    SELECT 
                        p.id id, 
                        p.firstname firstname,
                        p.lastname lastname,
                        p.address_id address_id,
                        COUNT(*) OVER() as totalCount
                    FROM person p
                    $joinsStr
                    $predicateStr
                    $orderStr
                """.trimIndent().wrapPaging(input._pageNum, input._fetch))
        .unwrap(org.hibernate.query.NativeQuery::class.java)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)

        val result = mutableListOf<PersonEntity>()
        var totalCount = 0

        q.resultList.forEach {
            val map = it as Map<String, Any>
            val konto = PersonEntity().apply {
                totalCount = Conversion.asInt(map["TOTALCOUNT"])
                val bbId = ByteBuffer.wrap(map["ID"] as ByteArray)
                val bbAddr = ByteBuffer.wrap(map["ADDRESS_ID"] as ByteArray)

                this.id = UUID(bbId.long, bbId.long)
                this.address_id = UUID(bbAddr.long, bbAddr.long)

                this.firstname = map["FIRSTNAME"] as String
                this.lastname = map["LASTNAME"] as String
            }
            result.add(konto)
        }
        return Pair(result, totalCount)
    }
}

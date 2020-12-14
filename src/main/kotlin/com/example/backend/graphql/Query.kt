package com.example.backend.graphql

import com.example.backend.service.PersonSearchService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.ceil
import kotlin.math.min
import kotlin.system.measureTimeMillis

@Component
class Query : GraphQLQueryResolver {

    @Autowired
    lateinit var personSearchService: PersonSearchService

    @Suppress("unused")
    fun personSearch(input: Search): SearchResult<Person> {

        var searchResult: Pair<List<Person?>, Int> = Pair(emptyList(), 0)
        val time = measureTimeMillis {
            searchResult = personSearchService.find(input)
        }

        val totalCount = searchResult.second
        val pageCount = ceil(totalCount.toDouble() / input._fetch).toInt()
        val pageNum = min(pageCount, input._pageNum)

        return SearchResult(
                totalCount,
                time,
                pageCount,
                pageNum,
                input._fetch,
                pageNum < pageCount,
                searchResult.first
        )
    }
}


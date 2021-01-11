package com.example.backend.service

import com.example.backend.domain.AddressEntity
import com.example.backend.domain.CityEntity
import com.example.backend.domain.PersonEntity
import com.example.backend.graphql.Address
import com.example.backend.graphql.City
import com.example.backend.graphql.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoadingService {

    @Autowired
    lateinit var repositoryService: RepositoryService

    fun loadAddressById(id: UUID): Address? {
        val entity = repositoryService.addressCrudRepository.findById(id)
        return if(entity.isPresent) buildAddress(entity.get()) else null
    }

    fun buildPerson(personEntity: PersonEntity): Person {
        return Person (
            id = personEntity.id!!,
            firstname = personEntity.firstname,
            lastname = personEntity.lastname,
            addressId = personEntity.address_id,
            loader = this
        )
    }

    private fun buildAddress(addressEntity: AddressEntity) = Address (
        id = addressEntity.id!!,
        street = addressEntity.street,
        city = buildCity(addressEntity.city)
    )

    private fun buildCity(cityEntity: CityEntity) = City (
        id = cityEntity.id!!,
        name = cityEntity.name,
        state = cityEntity.country
    )
}


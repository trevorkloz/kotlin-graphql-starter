package com.example.backend.service

import com.example.backend.domain.AddressEntity
import com.example.backend.domain.CityEntity
import com.example.backend.domain.PersonEntity
import com.example.backend.graphql.Address
import com.example.backend.graphql.City
import com.example.backend.graphql.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoadingService {

    @Autowired
    lateinit var repositoryService: RepositoryService

    fun loadAddressesById(id: Long): Collection<Address>? {
       return repositoryService.addressCrudRepository.findAddressById(id).map {
           buildAddress(it)
       }
    }

    fun buildPerson(personEntity: PersonEntity): Person {
        return Person (
            id = personEntity.id,
            firstname = personEntity.firstname,
            lastname = personEntity.lastname,
            addressId = personEntity.address_id,
            loader = this
        )
    }

    private fun buildAddress(addressEntity: AddressEntity) = Address (
        id = addressEntity.id,
        street = addressEntity.street,
        city = addressEntity.city?.let { buildCity(it) }
    )

    private fun buildCity(cityEntity: CityEntity) = City (
        id = cityEntity.id,
        name = cityEntity.name,
        state = cityEntity.country
    )
}


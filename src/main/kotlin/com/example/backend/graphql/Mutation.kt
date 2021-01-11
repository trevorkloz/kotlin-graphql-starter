package com.example.backend.graphql

import com.example.backend.domain.AddressEntity
import com.example.backend.domain.CityEntity
import com.example.backend.domain.PersonEntity
import com.example.backend.graphql.input.PersonInput
import com.example.backend.repository.AddressCrudRepository
import com.example.backend.repository.CityCrudRepository
import com.example.backend.repository.PersonCrudRepository
import com.example.backend.service.LoadingService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
@Component
class Mutation : GraphQLMutationResolver {

    @Autowired
    lateinit var loadingService: LoadingService

    @Autowired
    lateinit var personCrudRepository: PersonCrudRepository

    @Autowired
    lateinit var addressCrudRepository: AddressCrudRepository

    @Autowired
    lateinit var cityCrudRepository: CityCrudRepository

    @Transactional
    fun createPerson(person: PersonInput): Person {

            var city = cityCrudRepository.findCityByNameAndCountry(person.address.city.name, person.address.city.state)
        if (city == null) {
            city = cityCrudRepository.save(CityEntity().apply {
                this.name = person.address.city.name
                this.country = person.address.city.state
            })
        }

        val address = addressCrudRepository.save(AddressEntity().apply {
            this.street = person.address.street
            this.city = city
        })

        val person = personCrudRepository.save(PersonEntity().apply {
            this.firstname = person.firstname
            this.lastname = person.lastname
            this.address_id = address.id!!
        })

        return loadingService.buildPerson(person)
    }
}


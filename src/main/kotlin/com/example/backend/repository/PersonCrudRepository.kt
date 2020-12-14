package com.example.backend.repository

import com.example.backend.domain.PersonEntity
import org.springframework.data.repository.CrudRepository

interface PersonCrudRepository : CrudRepository<PersonEntity?, Long?> {

    fun findPersonByid(id: Long?): Iterable<PersonEntity>

}
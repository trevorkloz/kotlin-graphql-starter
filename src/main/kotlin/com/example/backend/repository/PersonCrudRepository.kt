package com.example.backend.repository

import com.example.backend.domain.PersonEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PersonCrudRepository : CrudRepository<PersonEntity?, UUID?> {

}
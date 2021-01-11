package com.example.backend.repository

import com.example.backend.domain.CityEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CityCrudRepository : CrudRepository<CityEntity?, UUID?> {

    fun findCityByNameAndCountry(name: String, country: String): CityEntity?

}
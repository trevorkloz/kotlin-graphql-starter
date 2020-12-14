package com.example.backend.repository

import com.example.backend.domain.AddressEntity
import org.springframework.data.repository.CrudRepository

interface AddressCrudRepository : CrudRepository<AddressEntity?, Long?> {

    fun findAddressById(id: Long?): Iterable<AddressEntity>

}
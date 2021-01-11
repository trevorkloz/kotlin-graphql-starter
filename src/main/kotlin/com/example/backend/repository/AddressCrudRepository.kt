package com.example.backend.repository

import com.example.backend.domain.AddressEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AddressCrudRepository : CrudRepository<AddressEntity?, UUID?> {

}
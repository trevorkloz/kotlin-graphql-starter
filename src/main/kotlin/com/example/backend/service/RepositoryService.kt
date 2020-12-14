package com.example.backend.service

import com.example.backend.repository.AddressCrudRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RepositoryService {

    @Autowired
    lateinit var addressCrudRepository: AddressCrudRepository

}
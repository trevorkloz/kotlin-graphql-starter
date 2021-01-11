package com.example.backend.graphql

import java.util.*

open class Address (
        val id: UUID,
        val street: String,
        val city: City
)


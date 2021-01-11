package com.example.backend.domain

import java.util.*
import javax.persistence.*

@Table(name = "person")
@Entity
class PersonEntity {

    @Id
    @GeneratedValue
    var id: UUID? = null

    lateinit var firstname: String

    lateinit var lastname: String

    lateinit var address_id: UUID
}
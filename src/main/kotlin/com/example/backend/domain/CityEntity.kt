package com.example.backend.domain

import java.util.*
import javax.persistence.*

@Table(name = "city")
@Entity
class CityEntity {

    @Id
    @GeneratedValue
    var id: UUID? = null

    lateinit var name: String

    lateinit var country: String
}

package com.example.backend.domain

import javax.persistence.*

@Table(name = "person")
@Entity
class PersonEntity {

    @Id
    var id: Long = 0L

    var firstname: String? = null

    var lastname: String? = null

    var address_id: Long = 0L
}
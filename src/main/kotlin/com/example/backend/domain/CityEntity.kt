package com.example.backend.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "city")
@Entity
class CityEntity {

    @Id
    var id: Long = 0L

    var name: String? = null

    var country: String? = null
}

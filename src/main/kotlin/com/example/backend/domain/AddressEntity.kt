package com.example.backend.domain

import java.util.*
import javax.persistence.*

@Table(name = "address")
@Entity
class AddressEntity {

    @Id
    @GeneratedValue
    var id: UUID? = null

    lateinit var street: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id", nullable=false)
    lateinit var city: CityEntity

}
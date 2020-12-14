package com.example.backend.domain

import javax.persistence.*

@Table(name = "address")
@Entity
class AddressEntity {

    @Id
    var id: Long = 0L

    var street: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id", nullable=false)
    var city: CityEntity? = null

}
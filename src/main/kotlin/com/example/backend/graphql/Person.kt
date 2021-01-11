package com.example.backend.graphql

import com.example.backend.service.LoadingService
import org.springframework.scheduling.annotation.Async
import java.util.*
import java.util.concurrent.CompletableFuture

open class Person (
        val id: UUID,
        val lastname: String,
        val firstname: String,
        val addressId: UUID,
        private val loader: LoadingService
) {
        @Async
        open fun address() : CompletableFuture<Address> {
            return CompletableFuture.completedFuture(loader.loadAddressById(addressId))
        }
}


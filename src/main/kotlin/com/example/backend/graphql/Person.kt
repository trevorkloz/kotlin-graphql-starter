package com.example.backend.graphql

import com.example.backend.service.LoadingService
import org.springframework.scheduling.annotation.Async
import java.util.concurrent.CompletableFuture

open class Person (
        val id: Long,
        val lastname: String?,
        val firstname: String?,
        val addressId: Long,
        private val loader: LoadingService
) {
        @Async
        open fun address() : CompletableFuture<Collection<Address>> {
            return CompletableFuture.completedFuture(loader.loadAddressesById(addressId))
        }
}


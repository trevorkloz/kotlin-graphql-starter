package com.example.backend.tooling

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

@Configuration
class GraphQLTestConfiguration {

    @Value("classpath:graphql/query-wrapper.json")
    private lateinit var queryWrapperFile: Resource

    @Value("classpath:graphql/search.graphql")
    private lateinit var searchGraphQL: Resource

    @Bean
    fun searchGraphQL(): String {
        return StreamUtils.copyToString(searchGraphQL.inputStream, StandardCharsets.UTF_8)
    }

    @Bean
    fun queryWrapper(): String {
        return StreamUtils.copyToString(queryWrapperFile.inputStream, StandardCharsets.UTF_8)
    }

}
package com.example.backend.tooling

import com.fasterxml.jackson.core.io.JsonStringEncoder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


@Component
class GraphQLIntegrationTestUtils {

    @Autowired
    private lateinit var queryWrapper: String

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val jsonStringEncoder = JsonStringEncoder.getInstance()

    /**
     * Call this method with a valid GraphQL query/mutation String
     * This function will escape it properly and wrap it into a JSON query object
     */
    fun createJsonQuery(graphQL: String, variables: String? = null): String {
        var q = queryWrapper
                .replace("__payload__", escape(graphQL))
        variables?.let { q = q.replace("__variables__", escape(variables)) }
        return q
    }

    fun escape(string: String): String {
        return jsonStringEncoder.quoteAsString(
                string.replace("'", "\"")).joinToString("")
    }

    companion object {
        const val ENDPOINT_LOCATION: String = "/graphql"
    }

    fun parse(responseEntity: ResponseEntity<String>): GraphQLResponse {
        return GraphQLResponse(responseEntity, objectMapper)
    }
}
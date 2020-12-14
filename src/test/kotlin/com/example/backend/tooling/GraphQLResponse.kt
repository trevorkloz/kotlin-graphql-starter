package com.example.backend.tooling

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class GraphQLResponse(responseEntity: ResponseEntity<String>, objectMapper: ObjectMapper) {

    private val rawResponse: ResponseEntity<String> = Objects.requireNonNull(responseEntity)
    private val mapper: ObjectMapper = Objects.requireNonNull(objectMapper)
    private val context: ReadContext

    operator fun get(path: String): Any {
        return get(path, Any::class.java)
    }

    fun <T> list(path: String, valueType: Class<T>): List<T> {
        val raw: List<*> = context.read(path, MutableList::class.java)
        return mapper.convertValue(raw, mapper.typeFactory.constructCollectionType(MutableList::class.java, valueType))
    }

    operator fun <T> get(path: String?, type: Class<T>): T {
        return mapper.convertValue(context.read(path, Any::class.java), type)
    }

    private fun getErrors(): List<Any> {
        return list("$.errors", Any::class.java)
    }

    val isOk: Boolean
        get() = statusCode == HttpStatus.OK
    private val statusCode: HttpStatus
        get() = rawResponse.statusCode

    init {
        Objects.requireNonNull(responseEntity.body,
                { "Body is empty with status " + responseEntity.statusCodeValue })
        context = JsonPath.parse(responseEntity.body)
    }
}
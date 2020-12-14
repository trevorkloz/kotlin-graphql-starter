package com.example.backend.configuration

import graphql.GraphQLError
import graphql.execution.DataFetcherResult
import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GraphQLValidationConfiguration {

    @Bean
    fun directiveWiring(): SchemaDirectiveWiring {
        val validationRules = ValidationRules.newValidationRules()
                .onValidationErrorStrategy(ThrowExceptionStrategy())
                .build()
        return ValidationSchemaWiring(validationRules)
    }
}

class ThrowExceptionStrategy: OnValidationErrorStrategy {
    override fun shouldContinue(errors: List<GraphQLError?>?, environment: DataFetchingEnvironment?): Boolean {
        return false
    }

    override fun onErrorValue(errors: List<GraphQLError?>?, environment: DataFetchingEnvironment?): Any? {
        return DataFetcherResult.newResult<Any?>().errors(errors).data(null).build()
    }
}

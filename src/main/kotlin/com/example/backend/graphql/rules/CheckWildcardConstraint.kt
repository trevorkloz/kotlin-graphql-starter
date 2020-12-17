package com.example.backend.graphql.rules

import graphql.GraphQLError
import graphql.Scalars
import graphql.kickstart.execution.error.GenericGraphQLError
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLTypeUtil
import graphql.validation.constraints.AbstractDirectiveConstraint
import graphql.validation.constraints.Documentation
import graphql.validation.rules.ValidationEnvironment

class CheckWildcardConstraint : AbstractDirectiveConstraint("Check") {

    override fun getDocumentation(): Documentation {
        return Documentation.newDocumentation()
            .messageTemplate(messageTemplate)
            .description("The element must be ...")
            .directiveSDL(
                "directive @Check(String = \"%s\") " +
                        "on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION",
                messageTemplate
            ).build()
    }

    override fun appliesToType(inputType: GraphQLInputType?): Boolean {
        return isOneOfTheseTypes(
            inputType,
            Scalars.GraphQLString
        )
    }

    override fun runConstraint(validationEnvironment: ValidationEnvironment): List<GraphQLError?> {
        val validatedValue = validationEnvironment.validatedValue
        val argumentType = validationEnvironment.validatedType

        if (validatedValue == null) {
            return emptyList()
        }

        val validatedValues: List<Any> = if (GraphQLTypeUtil.isList(argumentType)) {
            @Suppress("UNCHECKED_CAST")
            validatedValue as List<Any>
        } else {
            listOf(validatedValue)
        }

        for (value in validatedValues) {
            if (value is String && (value.contains("%") || value.contains("*"))) {
                return listOf(GenericGraphQLError("Wildcards not allowed."))
            }
        }
        return emptyList()
    }
}
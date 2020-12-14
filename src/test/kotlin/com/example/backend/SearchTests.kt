package com.example.backend

import com.example.backend.tooling.GraphQLIntegrationTestUtils
import com.example.backend.tooling.GraphQLResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchTests {

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Autowired
	private lateinit var graphQLIntegrationTestUtils: GraphQLIntegrationTestUtils

	@Autowired
	private lateinit var searchGraphQL: String

	fun send(query: String, variables: String?): ResponseEntity<String> {
		val payload: String = graphQLIntegrationTestUtils.createJsonQuery(
				query, variables)

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON

		return restTemplate.exchange(GraphQLIntegrationTestUtils.ENDPOINT_LOCATION,
				HttpMethod.POST, HttpEntity(payload, headers), String::class.java)
	}

	fun sendAndParse(query: String, variables: String?): GraphQLResponse {
		return graphQLIntegrationTestUtils.parse(send(query, variables))
	}

	@Test
	fun `When searching x results are returned`() {
		val response = sendAndParse(searchGraphQL, """{
							"input": {
								"lastname": "Cresswell"
							}
						}""")

		assertThat(response.isOk).isTrue()
		assertThat(response.list("$.data.personSearch.objects", Any::class.java).size).isEqualTo(1)
		assertThat(response["$.data.personSearch._totalCount"]).isEqualTo(1)

		assertThat(response["$.data.personSearch.objects[0].id"]).isEqualTo(2)
		assertThat(response["$.data.personSearch.objects[0].firstname"]).isEqualTo("Mickie")
		assertThat(response["$.data.personSearch.objects[0].lastname"]).isEqualTo("Cresswell")

	}

}

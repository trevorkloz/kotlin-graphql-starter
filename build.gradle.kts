import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.1.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"

}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.h2database:h2")
	implementation("com.oracle.database.jdbc:ojdbc8:19.6.0.0")
	implementation("com.oracle.database.jdbc:ucp:19.6.0.0")

	implementation("com.graphql-java-kickstart:graphql-java-tools:6.2.0")
	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:8.0.0")
	implementation("com.graphql-java:graphql-java-extended-validation:15.0.1")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("com.google.code.gson:gson:2.8.6")
	implementation("com.google.guava:guava:30.0-jre")

	runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:8.0.0")
	runtimeOnly("com.graphql-java-kickstart:voyager-spring-boot-starter:8.0.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.skyscreamer:jsonassert:1.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

}

tasks.withType<Test> {
	systemProperty("illegal-access", "info")
	useJUnitPlatform()

	useJUnitPlatform()

	testLogging {
		lifecycle {
			events = mutableSetOf(
				TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
			exceptionFormat = TestExceptionFormat.FULL
			showExceptions = true
			showCauses = true
			showStackTraces = true
			showStandardStreams = true
		}
		info.events = lifecycle.events
		info.exceptionFormat = lifecycle.exceptionFormat
	}

	val failedTests = mutableListOf<TestDescriptor>()
	val skippedTests = mutableListOf<TestDescriptor>()

	// See https://github.com/gradle/kotlin-dsl/issues/836
	addTestListener(object : TestListener {
		override fun beforeSuite(suite: TestDescriptor) {}
		override fun beforeTest(testDescriptor: TestDescriptor) {}
		override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
			when (result.resultType) {
				TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
				TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
				else -> Unit
			}
		}

		override fun afterSuite(suite: TestDescriptor, result: TestResult) {
			if (suite.parent == null) { // root suite
				logger.lifecycle("----")
				logger.lifecycle("Test result: ${result.resultType}")
				logger.lifecycle(
					"Test summary: ${result.testCount} tests, " +
							"${result.successfulTestCount} succeeded, " +
							"${result.failedTestCount} failed, " +
							"${result.skippedTestCount} skipped")
				failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
				skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
			}
		}

		private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
			logger.lifecycle(subject)
			forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
		}

		private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: "$name"
	})
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Logging

plugins {
	alias(libs.plugins.springframework.boot)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.plugin.spring)
	alias(libs.plugins.jooq)
	alias(libs.plugins.diktat)
	alias(libs.plugins.detekt)
	jacoco
	alias(libs.plugins.sonarqube)
	alias(libs.plugins.versions)
	alias(libs.plugins.version.catalog.update)
}

group = "ch.learnup"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(enforcedPlatform(libs.kotlin.bom))
	implementation(enforcedPlatform(libs.spring.framework.bom))
	api(enforcedPlatform(libs.spring.boot.dependencies)) {
		exclude("org.yaml", "snakeyaml") // pull version from jackson (fix vulnerabilities)
	}

	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.jooq)
	implementation(libs.liquibase.core)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.jackson.module.kotlin)
	implementation(libs.jooq)
	implementation(libs.jooq.codegen)
	implementation(libs.kotlin.reflect)
	implementation(libs.mariadb.java.client)
	implementation(libs.springdoc.openapi.ui)
	implementation(libs.springdoc.openapi.kotlin)

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.archunit)
	testImplementation(libs.testcontainers.bom)
	testImplementation(libs.org.testcontainers.mariadb)
	testImplementation(libs.mockserver)
	testImplementation(libs.mockserver.client.java)
	testImplementation(libs.spring.webflux)
	testImplementation(libs.mockito.kotlin)

	jooqGenerator(libs.mariadb.java.client)
	jooqGenerator(libs.jakarta.xml.bind.api)

	detektPlugins(libs.detekt.formatting)
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict") // https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_compiler_arguments
		jvmTarget = JavaVersion.VERSION_21.toString()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(false)
	}
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

detekt {
	source.setFrom("src/main/kotlin")
	config.setFrom("detekt.yml")
	parallel = true
}

diktat {
	diktatConfigFile = file("diktat-analysis.yml")
	inputs {
		include("src/main/kotlin/**/*.kt")
	}
}

tasks.matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }.first().apply {
	setDependsOn(dependsOn.filter { !(it is TaskProvider<*> && it.name == "detekt") })
	dependsOn("diktatCheck")
	dependsOn("detekt")
	// Run detektMain as well. See discussion https://github.com/detekt/detekt/issues/3122 and https://github.com/detekt/detekt/discussions/4959
	dependsOn("detektMain")
}

// Run detekt after diktat (diktat checks formatting errors that it can also fix)
tasks.matching { it.name == "detekt" }.first().mustRunAfter("diktatCheck")

// Required for detektMain not to pick up kotlin-gen
tasks.withType<Detekt>().configureEach {
	exclude {
		it.file.absolutePath.contains("kotlin-gen/")
	}
}

jooq {
	configurations {
		create("main") {
			generateSchemaSourceOnCompilation.set(false)
			jooqConfiguration.apply {
				logging = Logging.DEBUG
				jdbc.apply {
					driver = "org.mariadb.jdbc.Driver"
					url = "jdbc:mariadb://localhost:3309/learnup_backend"
					user = "backend"
					password = "backend"
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = "org.jooq.meta.mariadb.MariaDBDatabase"
						inputSchema = "learnup_backend"
						excludes = "Databasechangelog|Databasechangeloglock"
						forcedTypes.addAll(
							listOf(
								ForcedType().apply {
									userType = "java.util.UUID"
									isAutoConverter = true
									includeExpression = ".*id.*"
								},
							)
						)
					}
					generate.apply {
						isDaos = true
						isNonnullAnnotation = true
						isNullableAnnotation = true
						nullableAnnotationType = "org.jetbrains.annotations.Nullable"
						nonnullAnnotationType = "org.jetbrains.annotations.NotNull"
						isDeprecated = false
						isRecords = true
						isImmutablePojos = true
						isFluentSetters = true
						isKotlinNotNullPojoAttributes = true
						isKotlinNotNullRecordAttributes = true
					}
					target.apply {
						packageName = "ch.learnup.backend.persistence"
						directory = "src/main/kotlin-gen/jooq-gen"
					}
					strategy.apply {
						name = "org.jooq.codegen.DefaultGeneratorStrategy"
						matchers = Matchers().apply {
							tables.add(
								MatchersTableType().apply {
									expression = "^t_(.*)$"
									tableIdentifier = MatcherRule().apply {
										expression = "$1"
										transform = MatcherTransformType.UPPER
									}
									tableClass = MatcherRule().apply {
										expression = "$1_TABLE"
										transform = MatcherTransformType.PASCAL
									}
									daoClass = MatcherRule().apply {
										expression = "$1_DAO"
										transform = MatcherTransformType.PASCAL
									}
									pojoClass = MatcherRule().apply {
										expression = "$1"
										transform = MatcherTransformType.PASCAL
									}
									recordClass = MatcherRule().apply {
										expression = "$1_RECORD"
										transform = MatcherTransformType.PASCAL
									}
								},
							)
						}
					}
				}
			}
		}
	}
}

sonar {
	properties {
		property("sonar.projectKey", "learnUP_backend")
		property("sonar.organization", "deepspace1000")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.sources", "src/main")
		property("sonar.tests", "src/test")
		property("sonar.log.level", "DEBUG")
		property("sonar.projectName", "learnUP_backend")

		property("sonar.exclusions", "src/main/kotlin-gen/jooq-gen/**/*")
		property("sonar.cpd.exclusions", "src/main/**/*.sql")

		property("sonar.issue.ignore.multicriteria", "duplicatedStringsInSql,setCallsInJooq")
		property("sonar.issue.ignore.multicriteria.duplicatedStringsInSql.ruleKey", "plsql:S1192")
		property("sonar.issue.ignore.multicriteria.duplicatedStringsInSql.resourceKey", "**/*.sql")
		property("sonar.issue.ignore.multicriteria.setCallsInJooq.ruleKey", "kotlin:S6518")
		property("sonar.issue.ignore.multicriteria.setCallsInJooq.resourceKey", "**/*Queries.kt")

	}
}

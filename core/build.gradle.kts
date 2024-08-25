import com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.jooq)
}

group = "ch.learntrack"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.spring.boot.starter.jooq)
    implementation(libs.liquibase.core)
    implementation(libs.jooq)
    implementation(libs.jooq.codegen)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    runtimeOnly(libs.postgresql)

    jooqGenerator(libs.postgresql)
    jooqGenerator(libs.jakarta.xml.bind.api)
}

// Required for detektMain not to pick up kotlin-gen
tasks.withType<Detekt>().configureEach {
    exclude {
        it.file.absolutePath.contains("kotlin-gen/")
    }
}

detekt {
    source.setFrom("src/main/kotlin")
    config.setFrom("detekt.yml")
    buildUponDefaultConfig = true
    parallel = true
}

ktfmt { kotlinLangStyle() }

tasks.register<KtfmtFormatTask>("ktfmtPrecommit") {
    val filesToLint: String? by project
    val filesToLintArray = filesToLint?.split("\n")?.filter { it.isNotEmpty() }
    source = project.fileTree(project.projectDir)
    if (filesToLintArray != null) {
        val array = filesToLintArray.toTypedArray()
        include(*array)
    } else {
        include("**/*.kt")
    }
}

tasks
    .matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }
    .first()
    .apply {
        setDependsOn(dependsOn.filter { !(it is TaskProvider<*> && it.name == "detekt") })
        dependsOn("ktfmtCheck")
        dependsOn("detekt")
        // Run detektMain as well. See discussion https://github.com/detekt/detekt/issues/3122 and
        // https://github.com/detekt/detekt/discussions/4959
        dependsOn("detektMain")
    }

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = Logging.DEBUG
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5517/learntrack_backend"
                    user = "backend"
                    password = "backend"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "Databasechangelog|Databasechangeloglock"
                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                },
                                ForcedType().apply {
                                    userType = "ch.learntrack.core.persistence.UserRole"
                                    withEnumConverter(true)
                                    includeExpression = "user_role"
                                }
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
                        visibilityModifier = VisibilityModifier.PUBLIC
                    }
                    target.apply {
                        packageName = "ch.learntrack.core.persistence"
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

liquibase {
    activities.register("main") {
        this.arguments= mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
            "url" to "jdbc:postgresql://localhost:5517/learntrack_backend",
            "username" to "backend",
            "password" to "backend"
        )
    }
    runList = "main"
}
1

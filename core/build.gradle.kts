import com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.jvm)
    alias(libs.plugins.ktor.plugin)
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
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
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
1

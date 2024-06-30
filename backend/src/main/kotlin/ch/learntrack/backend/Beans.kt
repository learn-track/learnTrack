package ch.learntrack.backend

import ch.learntrack.backend.admin.adminBeans
import ch.learntrack.backend.backoffice.backofficeBeans
import ch.learntrack.backend.common.LearnTrackExceptionHandler
import ch.learntrack.backend.email.emailBeans
import ch.learntrack.backend.grade.gradeBeans
import ch.learntrack.backend.jwt.jwtBeans
import ch.learntrack.backend.school.schoolBeans
import ch.learntrack.backend.security.securityBeans
import ch.learntrack.backend.subject.subjectBeans
import ch.learntrack.backend.user.userBeans
import ch.learntrack.backend.usergrade.userGradeBeans
import ch.learntrack.backend.userschool.userSchoolBeans
import ch.learntrack.backend.whoami.whoamiBeans
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

private val libraryBeans = beans {
    bean {
        GroupedOpenApi.builder()
            .group("learntrack-api")
            .pathsToMatch("/**")
            .build()
    }
    bean {
        GroupedOpenApi.builder()
            .group("learntrack-frontend-api")
            .pathsToMatch("/**")
            .pathsToExclude("/backoffice/**")
            .addOpenApiCustomizer(ref<OpenApiCustomizer>())
            .build()
    }
    bean {
        OpenApiCustomizer { openApi: OpenAPI ->
            // Mark nullable types as such in the OpenAPI spec to improve the generated TypeScript types
            openApi.components.schemas.values
                .filter { schema: Schema<*> -> schema.type == "object" }
                .forEach { schema: Schema<*> ->
                    if (schema.properties != null && schema.required != null) {
                        schema.properties.entries
                            .filter { prop -> !schema.required.contains(prop.key) }
                            .forEach { entry -> entry.value.setNullable(true) }
                    }
                }
        }
    }
}

private val handlerBeans = beans {
    bean<LearnTrackExceptionHandler>()
}

public val coreBeans: List<BeanDefinitionDsl> = listOf(
    emailBeans,
    libraryBeans,
    handlerBeans,
    userBeans,
    jwtBeans,
    securityBeans,
    whoamiBeans,
    gradeBeans,
    schoolBeans,
    subjectBeans,
    userSchoolBeans,
    userGradeBeans,
)

public val beans: List<BeanDefinitionDsl> = mutableListOf<BeanDefinitionDsl>().apply {
    addAll(coreBeans)
    addAll(adminBeans)
    addAll(backofficeBeans)
}

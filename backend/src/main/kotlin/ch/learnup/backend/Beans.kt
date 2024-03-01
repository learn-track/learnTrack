package ch.learnup.backend

import ch.learnup.backend.user.userBeans

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.support.beans

private val libraryBeans = beans {
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

val beans = listOf(
    libraryBeans,
    userBeans,
)

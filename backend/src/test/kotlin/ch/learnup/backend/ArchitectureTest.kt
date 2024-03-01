package ch.learnup.backend

import com.tngtech.archunit.base.DescribedPredicate.describe
import com.tngtech.archunit.core.domain.JavaAccess.Predicates.target
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@AnalyzeClasses(
    packagesOf = [ArchitectureTest::class],
    importOptions = [ExcludeJooqGeneratedPackage::class],
)
class ArchitectureTest {
    @ArchTest
    val `there are no package cycles` =
        SlicesRuleDefinition.slices()
            .matching("ch.learnup.backend.(**)..")
            .should()
            .beFreeOfCycles()

    @ArchTest
    val `junit 4 is not used` =
        ArchRuleDefinition.methods().should().notBeAnnotatedWith("org.junit.Test")

    @ArchTest
    val `component and service annotation are not used` =
        ArchRuleDefinition.classes()
            .should()
            .notBeAnnotatedWith(Component::class.java)
            .andShould()
            .notBeAnnotatedWith(Service::class.java)
            .because(
                "We don't use component scan, use bean DSL! " +
                        "(@RestController is allowed because of @ResponseBody)",
            )

    @ArchTest
    val `no java collection streams are used` =
        ArchRuleDefinition.noClasses()
            .should()
            .callMethodWhere(
                target(
                    describe("is Collection.stream()") { target ->
                        "stream" == target.name && target.owner.isAssignableTo(Collection::class.java)
                    },
                ),
            )
}

internal class ExcludeJooqGeneratedPackage : ImportOption {
    override fun includes(location: Location): Boolean = !location.contains("ch/learnup/backend/persistence")
}

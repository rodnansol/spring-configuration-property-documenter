import org.rodnansol.core.generator.template.TemplateType
import org.rodnansol.gradle.tasks.AggregationInput
import org.rodnansol.gradle.tasks.GenerateAndAggregateDocumentsTask
import org.rodnansol.gradle.tasks.GeneratePropertyDocumentTask
import org.rodnansol.gradle.tasks.customization.AsciiDocTemplateCustomization
import org.rodnansol.gradle.tasks.customization.ContentCustomization
import org.rodnansol.gradle.tasks.customization.TemplateMode

buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("org.rodnansol:spring-configuration-property-documenter-gradle-plugin:999-SNAPSHOT")
    }
}


plugins {
    id("java")
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "org.rodnansol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

apply{
    plugin("org.rodnansol.spring-configuration-property-documenter")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<GenerateAndAggregateDocumentsTask>("aggregateProperties") {
    documentName = "Example Aggregated Configuration Options"
    documentDescription = "Example"
    type = TemplateType.ADOC
    isFailOnMissingInput = true
    outputFile = File("build/generated/aggregated.adoc")

    asciiDocCustomization(delegateClosureOf<AsciiDocTemplateCustomization> {
        isIncludeUnknownGroup = false
        isIncludeGenerationDate = false
        isRemoveEmptyGroups = true
        templateMode = TemplateMode.COMPACT
        contentCustomization(delegateClosureOf<ContentCustomization> {
            isIncludeDeprecation = false
        })
    })

    outputs.upToDateWhen { false }
}

tasks.register<GeneratePropertyDocumentTask>("generateProperties") {
    documentName = "Example Configuration Options"
    documentDescription = "Example"
    type = TemplateType.ADOC
    failOnMissingInput = true

    asciiDocCustomization(delegateClosureOf<AsciiDocTemplateCustomization> {
        isIncludeUnknownGroup = false
        isIncludeGenerationDate = false
        isRemoveEmptyGroups = true
        templateMode = TemplateMode.COMPACT
        contentCustomization(delegateClosureOf<ContentCustomization> {
            isIncludeDeprecation = false
        })
    })

    outputs.upToDateWhen { false }
}
package org.rodnansol.core;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.MetadataReader;
import org.rodnansol.core.generator.Property;
import org.rodnansol.core.generator.PropertyGroup;

import java.io.File;
import java.util.List;
import java.util.Map;

class MetadataReaderTest {

    private final MetadataReader underTest = new MetadataReader();

    @Test
    void readProperties_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() {
        // Given

        // When
        Map<String, List<Property>> stringListMap = underTest.readProperties(new File("src/test/resources/spring-configuration-metadata.json"));

        // Then
        System.out.println(stringListMap);
    }

    @Test
    void readProperties2_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() {
        // Given

        // When
        List<PropertyGroup> propertyGroups = underTest.readProperties2(new File("src/test/resources/spring-configuration-metadata.json"));

        // Then
        System.out.println(propertyGroups);
    }
}

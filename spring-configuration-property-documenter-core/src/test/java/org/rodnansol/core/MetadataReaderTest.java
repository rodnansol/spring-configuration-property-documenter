package org.rodnansol.core;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.template.Property;
import org.rodnansol.core.generator.template.PropertyGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

class MetadataReaderTest {

    private final MetadataReader underTest = MetadataReader.INSTANCE;

    @Test
    void readProperties_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() {
        // Given

        // When
        Map<String, List<Property>> stringListMap = underTest.readPropertiesAsMap(new File("src/test/resources/spring-configuration-metadata.json"));

        // Then
        System.out.println(stringListMap);
    }

    @Test
    void readProperties2_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() throws FileNotFoundException {
        // Given

        // When
        List<PropertyGroup> propertyGroups = underTest.readPropertiesAsList(new FileInputStream("src/test/resources/spring-configuration-metadata.json"));

        // Then
        System.out.println(propertyGroups);
    }
}

package org.rodnansol.core;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.reader.MetadataConversionException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.template.Property;
import org.rodnansol.core.generator.template.PropertyDeprecation;
import org.rodnansol.core.generator.template.PropertyGroup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class MetadataReaderTest {

    private final MetadataReader underTest = MetadataReader.INSTANCE;

    @Test
    void readPropertiesAsMap_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() throws FileNotFoundException {
        // Given

        // When
        Map<String, List<Property>> resultMap = underTest.readPropertiesAsMap(new FileInputStream("src/test/resources/spring-configuration-metadata.json"));

        // Then
        Map<String, List<Property>> expectedResultMap = Map.ofEntries(
            entry("com.example.springpropertysources.YourProperties", List.of(new Property("this.is.your.property", "java.lang.String", null, "This is YOUR property.", null, null))),
            entry("com.example.springpropertysources.MyProperties", List.of(
                new Property("this.is.my.another-variable", "java.lang.String", null, null, "with default value", new PropertyDeprecation(null, null)),
                new Property("this.is.my.date", "java.time.LocalDate", null, null, null, null),
                new Property("this.is.my.date-time", "java.time.LocalDateTime", null, null, null, null),
                new Property("this.is.my.duration", "java.time.Duration", null, "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")),
                new Property("this.is.my.instant", "java.time.Instant", null, null, "123", null),
                new Property("this.is.my.variable", "java.lang.String", null, "This is my variable.", null, null)
            )),
            entry("com.example.springpropertysources.FirstLevelNestedProperty", List.of(
                new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", null, "Description of this thing.", "123", null),
                new Property("this.is.my.first-level-nested-property.name", "java.lang.String", null, "Name of the custom property.", "ABC", null)
            )),
            entry("com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", List.of(
                new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", null, "Custom nested", null, null)
            )),
            entry("com.example.springpropertysources.TopLevelClassNestedProperty", List.of(
                new Property("this.is.my.nested.nested-value", "java.lang.String", null, "Nested value.", null, null)
            ))
        );
        assertThat(resultMap)
            .containsAllEntriesOf(expectedResultMap);
    }

    @Test
    void readPropertiesAsPropertyGroupList_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties() throws FileNotFoundException {
        // Given

        // When
        List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream("src/test/resources/spring-configuration-metadata.json"));

        // Then
        PropertyGroup expectedYourProperties = new PropertyGroup("this.is.your", "com.example.springpropertysources.YourProperties", "com.example.springpropertysources.YourProperties");
        expectedYourProperties.addProperty(new Property("this.is.your.property", "java.lang.String", "property", "This is YOUR property.", null, null));

        PropertyGroup expectedMyProperties = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
        expectedMyProperties.addProperty(new Property("this.is.my.another-variable", "java.lang.String", "another-variable", null, "with default value", new PropertyDeprecation(null, null)));
        expectedMyProperties.addProperty(new Property("this.is.my.date", "java.time.LocalDate", "date", null, null, null));
        expectedMyProperties.addProperty(new Property("this.is.my.date-time", "java.time.LocalDateTime", "date-time", null, null, null));
        expectedMyProperties.addProperty(new Property("this.is.my.duration", "java.time.Duration", "duration", "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")));
        expectedMyProperties.addProperty(new Property("this.is.my.instant", "java.time.Instant", "instant", null, "123", null));
        expectedMyProperties.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));

        PropertyGroup expectedFirstLevelNestedProperty = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
        expectedFirstLevelNestedProperty.setNested(true);
        expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
        expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
        expectedFirstLevelNestedProperty.setParentGroup(expectedMyProperties);

        PropertyGroup firstLevelNestedPropertySecondLevelNestedClass = new PropertyGroup("this.is.my.first-level-nested-property.second-level-nested-class", "com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", "com.example.springpropertysources.FirstLevelNestedProperty");
        firstLevelNestedPropertySecondLevelNestedClass.setNested(true);
        firstLevelNestedPropertySecondLevelNestedClass.addProperty(new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", "second-level-value", "Custom nested", null, null));

        PropertyGroup expectedNestedProperties = new PropertyGroup("this.is.my.nested", "com.example.springpropertysources.TopLevelClassNestedProperty", "com.example.springpropertysources.TopLevelClassNestedProperty");
        expectedNestedProperties.addProperty(new Property("this.is.my.nested.nested-value", "java.lang.String", "nested-value", "Nested value.", null, null));

        List<PropertyGroup> expectedPropertyGroups = List.of(expectedYourProperties, expectedMyProperties, expectedFirstLevelNestedProperty, firstLevelNestedPropertySecondLevelNestedClass, expectedNestedProperties);

        assertThat(propertyGroups)
            .containsAll(expectedPropertyGroups);
    }

    @Test
    void readPropertiesAsMap_shouldThrowMetadataConversionException_whenAnErrorOccursDuringConversion() throws FileNotFoundException {
        // Given

        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.readPropertiesAsMap(new FileInputStream("src/test/resources/spring-configuration-metadata-invalid.json")))
            .isInstanceOf(MetadataConversionException.class);
        // Then

    }

    @Test
    void readPropertiesAsPropertyGroupList_shouldThrowMetadataConversionException_whenAnErrorOccursDuringConversion() throws FileNotFoundException {
        // Given

        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.readPropertiesAsPropertyGroupList(new FileInputStream("src/test/resources/spring-configuration-metadata-invalid.json")))
            .isInstanceOf(MetadataConversionException.class);
        // Then

    }
}

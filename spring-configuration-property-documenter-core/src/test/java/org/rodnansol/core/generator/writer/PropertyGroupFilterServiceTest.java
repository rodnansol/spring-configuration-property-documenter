package org.rodnansol.core.generator.writer;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.Property;
import org.rodnansol.core.generator.template.PropertyDeprecation;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyGroupFilterServiceTest {

    PropertyGroupFilterService underTest = new PropertyGroupFilterService();

    @Test
    void removeEmptyGroupsIfNeeded_shouldRemoveEmptyGroup_whenConfigurationIsSetToTrue() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();
        asciiDocTemplateCustomization.setRemoveEmptyGroups(true);

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))),
            new PropertyGroup("Empty Test", "Type", "SourceType")
        ));
        underTest.removeEmptyGroupsIfNeeded(asciiDocTemplateCustomization, propertyGroups);

        // Then
        assertThat(propertyGroups).containsExactly(new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

    @Test
    void removeEmptyGroupsIfNeeded_shouldNotRemoveEmptyGroup_whenConfigurationIsSetToFalse() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();
        asciiDocTemplateCustomization.setRemoveEmptyGroups(false);

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))),
            new PropertyGroup("Empty Test", "Type", "SourceType")
        ));
        underTest.removeEmptyGroupsIfNeeded(asciiDocTemplateCustomization, propertyGroups);

        // Then
        assertThat(propertyGroups).containsExactly(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))),
            new PropertyGroup("Empty Test", "Type", "SourceType"));
    }

    @Test
    void removeUnknownGroupIfNeeded_shouldIncludeUnknownGroup_whenConfigurationIsSetToTrue() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();
        asciiDocTemplateCustomization.setIncludeUnknownGroup(true);

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test")))
        ));
        underTest.removeUnknownGroupIfNeeded(asciiDocTemplateCustomization, propertyGroups);

        // Then
        assertThat(propertyGroups).containsExactly(PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

    @Test
    void removeUnknownGroupIfNeeded_shouldNotIncludeUnknownGroup_whenConfigurationIsSetToTrue() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();
        asciiDocTemplateCustomization.setIncludeUnknownGroup(false);

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test")))
        ));
        underTest.removeUnknownGroupIfNeeded(asciiDocTemplateCustomization, propertyGroups);

        // Then
        assertThat(propertyGroups).containsExactly(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

    @Test
    void filterPropertyGroups_shouldRemoveTheNonIncludedGroups_whenIncludeListIsGiven() {
        // Given
        PropertyGroup propertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");

        PropertyGroup nestedPropertyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup, nestedPropertyGroup));

        // When
        underTest.filterPropertyGroups(propertyGroups, List.of("this.is.my"), List.of());

        // Then
        PropertyGroup expectedPropertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");


        assertThat(propertyGroups).containsExactly(expectedPropertyGroup);
    }

    @Test
    void filterPropertyGroups_shouldRemoveTheExcludedGroups_whenExcludeListIsGiven() {
        // Given
        PropertyGroup propertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");

        PropertyGroup nestedPropertyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup, nestedPropertyGroup));

        // When
        underTest.filterPropertyGroups(propertyGroups, List.of(), List.of("this.is.my"));

        // Then

        PropertyGroup expectedNestedProperyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");

        assertThat(propertyGroups).containsExactly(expectedNestedProperyGroup);
    }

    @Test
    void filterPropertyGroupProperties_shouldRemoveTheNonIncludedProperties_whenIncludeListIsGiven() {
        // Given
        PropertyGroup propertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
        propertyGroup.addProperty(new Property("this.is.my.another-variable", "java.lang.String", "another-variable", null, "with default value", new PropertyDeprecation(null, null)));
        propertyGroup.addProperty(new Property("this.is.my.date", "java.time.LocalDate", "date", null, null, null));
        propertyGroup.addProperty(new Property("this.is.my.date-time", "java.time.LocalDateTime", "date-time", null, null, null));
        propertyGroup.addProperty(new Property("this.is.my.duration", "java.time.Duration", "duration", "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")));
        propertyGroup.addProperty(new Property("this.is.my.instant", "java.time.Instant", "instant", null, "123", null));
        propertyGroup.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));

        PropertyGroup nestedPropertyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
        nestedPropertyGroup.setNested(true);
        nestedPropertyGroup.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
        nestedPropertyGroup.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
        nestedPropertyGroup.setParentGroup(propertyGroup);

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup, nestedPropertyGroup));

        // When
        underTest.filterPropertyGroupProperties(propertyGroups, List.of("this.is.my.another-variable", "this.is.my.instant", "this.is.my.first-level-nested-property.name"), List.of());

        // Then
        PropertyGroup expectedPropertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
        expectedPropertyGroup.addProperty(new Property("this.is.my.another-variable", "java.lang.String", "another-variable", null, "with default value", new PropertyDeprecation(null, null)));
        expectedPropertyGroup.addProperty(new Property("this.is.my.instant", "java.time.Instant", "instant", null, "123", null));

        PropertyGroup expectedNestedProperyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
        expectedNestedProperyGroup.setNested(true);
        expectedNestedProperyGroup.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
        expectedNestedProperyGroup.setParentGroup(expectedPropertyGroup);

        assertThat(propertyGroups).containsExactly(expectedPropertyGroup, expectedNestedProperyGroup);
    }

    @Test
    void filterPropertyGroupProperties_shouldRemoveTheExcludedProperties_whenExcludeListIsGiven() {
        // Given
        PropertyGroup propertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
        propertyGroup.addProperty(new Property("this.is.my.another-variable", "java.lang.String", "another-variable", null, "with default value", new PropertyDeprecation(null, null)));
        propertyGroup.addProperty(new Property("this.is.my.date", "java.time.LocalDate", "date", null, null, null));
        propertyGroup.addProperty(new Property("this.is.my.date-time", "java.time.LocalDateTime", "date-time", null, null, null));
        propertyGroup.addProperty(new Property("this.is.my.duration", "java.time.Duration", "duration", "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")));
        propertyGroup.addProperty(new Property("this.is.my.instant", "java.time.Instant", "instant", null, "123", null));
        propertyGroup.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));

        PropertyGroup nestedPropertyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
        nestedPropertyGroup.setNested(true);
        nestedPropertyGroup.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
        nestedPropertyGroup.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
        nestedPropertyGroup.setParentGroup(propertyGroup);

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup, nestedPropertyGroup));

        // When
        underTest.filterPropertyGroupProperties(propertyGroups, List.of(), List.of("this.is.my.another-variable", "this.is.my.instant", "this.is.my.first-level-nested-property.name"));

        // Then
        PropertyGroup expectedPropertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
        expectedPropertyGroup.addProperty(new Property("this.is.my.date", "java.time.LocalDate", "date", null, null, null));
        expectedPropertyGroup.addProperty(new Property("this.is.my.date-time", "java.time.LocalDateTime", "date-time", null, null, null));
        expectedPropertyGroup.addProperty(new Property("this.is.my.duration", "java.time.Duration", "duration", "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")));
        expectedPropertyGroup.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));


        PropertyGroup expectedNestedProperyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
        expectedNestedProperyGroup.setNested(true);
        expectedNestedProperyGroup.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
        expectedNestedProperyGroup.setParentGroup(expectedPropertyGroup);

        assertThat(propertyGroups).containsExactly(expectedPropertyGroup, expectedNestedProperyGroup);
    }
}

package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.PropertyGroup;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IncludeExcludeGroupPostProcessorTest {


    IncludeExcludeGroupPostProcessor underTest = new IncludeExcludeGroupPostProcessor();

    @Test
    void filterPropertyGroups_shouldRemoveTheNonIncludedGroups_whenIncludeListIsGiven() {
        // Given
        PropertyGroup propertyGroup = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");

        PropertyGroup nestedPropertyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup, nestedPropertyGroup));

        // When
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofGroupFilter(propertyGroups, List.of(), List.of("this.is.my")));

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
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofGroupFilter(propertyGroups, List.of("this.is.my"), List.of()));

        // Then

        PropertyGroup expectedNestedProperyGroup = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");

        assertThat(propertyGroups).containsExactly(expectedNestedProperyGroup);
    }
}

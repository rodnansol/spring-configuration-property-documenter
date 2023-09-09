package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyGroup;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialCharacterPostProcessorTest {

    SpecialCharacterPostProcessor underTest = new SpecialCharacterPostProcessor();

    @Test
    void postProcess_shouldReplaceSpecialCharacters_whenTemplateIsMarkdown() {
        // Given
        PropertyGroup propertyGroup = PropertyGroup.builder()
            .withGroupName("this.is.my")
            .withType("com.example.springpropertysources.MyProperties")
            .withSourceType("com.example.springpropertysources.MyProperties")
            .build();

        propertyGroup.addProperty(Property.builder("org.rodnansol.key", "java.lang.String")
            .withDescription("Test | description |")
            .withDefaultValue("Test | default value |")
            .build());

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup));

        // When
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(new MarkdownTemplateCustomization(), propertyGroups));

        // Then
        PropertyGroup expectedPropertyGroup = PropertyGroup.builder()
            .withGroupName("this.is.my")
            .withType("com.example.springpropertysources.MyProperties")
            .withSourceType("com.example.springpropertysources.MyProperties")
            .build();
        expectedPropertyGroup.addProperty(Property.builder("org.rodnansol.key", "java.lang.String")
            .withDescription("Test &#124 description &#124")
            .withDefaultValue("Test &#124 default value &#124")
            .build());

        assertThat(propertyGroups).containsExactly(expectedPropertyGroup);
    }

    @Test
    void postProcess_shouldReplaceSpecialCharacters_whenTemplateIsAsciidoc() {
        // Given
        PropertyGroup propertyGroup = PropertyGroup.builder()
            .withGroupName("this.is.my")
            .withType("com.example.springpropertysources.MyProperties")
            .withSourceType("com.example.springpropertysources.MyProperties")
            .build();

        propertyGroup.addProperty(Property.builder("org.rodnansol.key", "java.lang.String")
            .withDescription("Test | description |")
            .withDefaultValue("Test | default value |")
            .build());

        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(propertyGroup));

        // When
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(new AsciiDocTemplateCustomization(), propertyGroups));

        // Then
        PropertyGroup expectedPropertyGroup = PropertyGroup.builder()
            .withGroupName("this.is.my")
            .withType("com.example.springpropertysources.MyProperties")
            .withSourceType("com.example.springpropertysources.MyProperties")
            .build();
        expectedPropertyGroup.addProperty(Property.builder("org.rodnansol.key", "java.lang.String")
            .withDescription("Test \\| description \\|")
            .withDefaultValue("Test \\| default value \\|")
            .build());

        assertThat(propertyGroups).containsExactly(expectedPropertyGroup);
    }
}
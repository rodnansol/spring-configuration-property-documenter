package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmptyGroupRemovalPostProcessorTest {

    EmptyGroupRemovalPostProcessor underTest = new EmptyGroupRemovalPostProcessor();

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
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

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
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

        // Then
        assertThat(propertyGroups).containsExactly(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))),
            new PropertyGroup("Empty Test", "Type", "SourceType"));
    }

}

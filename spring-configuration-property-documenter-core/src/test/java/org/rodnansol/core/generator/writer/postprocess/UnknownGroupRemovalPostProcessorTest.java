package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownGroupRemovalPostProcessorTest {

    UnknownGroupRemovalPostProcessor underTest = new UnknownGroupRemovalPostProcessor();

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
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

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
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

        // Then
        assertThat(propertyGroups).containsExactly(
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

}

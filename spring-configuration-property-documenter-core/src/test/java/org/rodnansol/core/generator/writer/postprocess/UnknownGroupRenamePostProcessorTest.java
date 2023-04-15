package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownGroupRenamePostProcessorTest {

    UnknownGroupRenamePostProcessor underTest = new UnknownGroupRenamePostProcessor();

    @Test
    void renameUnknownGroup_shouldRenameUnknownGroup_whenItIsDifferentFromTheOriginal() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();
        asciiDocTemplateCustomization.setUnknownGroupLocalization("New name");

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test")))
        ));
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

        // Then
        PropertyGroup expectedUnknownGroup = PropertyGroup.createUnknownGroup();
        expectedUnknownGroup.setGroupName("New name");
        assertThat(propertyGroups).containsExactly(expectedUnknownGroup,
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

    @Test
    void renameUnknownGroup_shouldNotRenameUnknownGroup_whenItIsNotDifferentFromTheOriginal() {
        // Given
        AsciiDocTemplateCustomization asciiDocTemplateCustomization = new AsciiDocTemplateCustomization();

        // When
        List<PropertyGroup> propertyGroups = new ArrayList<>(List.of(
            PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test")))
        ));
        underTest.postProcess(PostProcessPropertyGroupsCommand.ofTemplate(asciiDocTemplateCustomization, propertyGroups));

        // Then
        assertThat(propertyGroups).containsExactly(
            PropertyGroup.createUnknownGroup(),
            new PropertyGroup("Not Empty Test", "Type", "SourceType", List.of(new Property("fqName", "test"))));
    }

}

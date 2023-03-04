package org.rodnansol.core.generator.writer.postprocess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PropertyGroupFilterServiceTest {

    PropertyGroupFilterService underTest;

    @Test
    void postProcessPropertyGroups_shouldCallPostProcessors() {
        // Given
        PostProcessPropertyGroupsCommand postProcessPropertyGroupsCommand = PostProcessPropertyGroupsCommand.ofTemplate(new MarkdownTemplateCustomization(), List.of());
        PropertyGroupPostProcessor propertyGroupPostProcessor = mock(PropertyGroupPostProcessor.class);

        // When
        underTest = new PropertyGroupFilterService(List.of(propertyGroupPostProcessor));
        underTest.postProcessPropertyGroups(postProcessPropertyGroupsCommand);

        // Then
        verify(propertyGroupPostProcessor).postProcess(postProcessPropertyGroupsCommand);

    }
}

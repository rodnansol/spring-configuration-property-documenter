package org.rodnansol.core.generator.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;
import org.rodnansol.core.generator.writer.postprocess.PostProcessPropertyGroupsCommand;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.verify;
import static io.qameta.allure.Allure.step;

@ExtendWith(MockitoExtension.class)
class AggregationDocumenterTest {

    @Mock
    File file;
    @Mock
    MetadataReader metadataReader;
    @Mock
    TemplateCompiler templateCompiler;
    @Mock
    MetadataInputResolverContext metadataInputResolverContext;
    @Mock
    PropertyGroupFilterService propertyGroupFilterService;
    @InjectMocks
    AggregationDocumenter underTest;

    @Test
    void filterGroupsAndProperties_shouldCallPropertyGroupFilterService() {
        step("Preparation", () -> {
            MarkdownTemplateCustomization templateCustomization = new MarkdownTemplateCustomization();
            CombinedInput combinedInput = new CombinedInput(file, "testSection", "testDescription");
            combinedInput.setExcludedGroups(List.of("excluded-group"));
            combinedInput.setExcludedProperties(List.of("excluded-property"));
            combinedInput.setIncludedGroups(List.of("included-group"));
            combinedInput.setIncludedProperties(List.of("included-property"));
            List<PropertyGroup> propertyGroups = List.of();
            step("Test logic", () -> {
                underTest.filterGroupsAndProperties(templateCustomization, combinedInput, propertyGroups);
                step("Assertions", () -> {
                    PostProcessPropertyGroupsCommand expectedCommand = new PostProcessPropertyGroupsCommand(templateCustomization, propertyGroups,
                        List.of("excluded-group"), List.of("included-group"),
                        List.of("excluded-property"), List.of("included-property"));
                    verify(propertyGroupFilterService).postProcessPropertyGroups(expectedCommand);
                });
            });
        });
    }
}

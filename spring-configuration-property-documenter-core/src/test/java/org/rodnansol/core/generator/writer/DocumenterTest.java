package org.rodnansol.core.generator.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.InputFileResolutionStrategy;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.data.MainTemplateData;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerMemoryStore;
import org.rodnansol.core.generator.template.TemplateMode;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.generator.writer.postprocess.PostProcessPropertyGroupsCommand;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.simple.SimpleProject;
import static io.qameta.allure.Allure.step;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumenterTest {

    protected static final String TEST_DESCRIPTION = "Test description";
    protected static final String TEST = "Test";

    @TempDir
    Path tempDir;

    @Mock
    InputStream inputStream;

    @Mock
    MetadataReader metadataReader;

    @Mock
    TemplateCompiler templateCompiler;

    @Mock
    MetadataInputResolverContext metadataInputResolverContext;

    @Mock
    PropertyGroupFilterService propertyGroupFilterService;

    @InjectMocks
    Documenter underTest;

    @Test
    void readMetadataAndGenerateRenderedFile_shouldWriteContentToFile() throws IOException {
        step("Preparation", () -> {
            SimpleProject project = ProjectFactory.ofSimpleProject(new File("."), TEST);
            File inputFile = new File("input-file");
            File output = tempDir.resolve("output-file").toFile();
            String singleTemplate = TemplateType.ADOC.getSingleTemplate(TemplateMode.STANDARD);
            CreateDocumentCommand command = getCreateDocumentCommand(project, inputFile, output, singleTemplate);
            step("Test logic", () -> {
                List<PropertyGroup> propertyGroups = List.of(
                    PropertyGroup.createUnknownGroup()
                        .addProperty(new Property("org.rodnansol.unknown-type", "java.lang.String")),
                    new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
                    new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
                );
                when(metadataInputResolverContext.getInputStreamFromFile(project, inputFile, InputFileResolutionStrategy.RETURN_EMPTY)).thenReturn(inputStream);
                when(metadataReader.readPropertiesAsPropertyGroupList(inputStream)).thenReturn(propertyGroups);
                when(templateCompiler.compileTemplate(eq(singleTemplate), any())).thenReturn("Hello World");
                when(templateCompiler.getMemoryStore()).thenReturn(mock(TemplateCompilerMemoryStore.class));
                underTest.readMetadataAndGenerateRenderedFile(command);
                step("Assertions", () -> {
                    MainTemplateData expectedTemplateData = new MainTemplateData(TEST, List.of(
                        PropertyGroup.createUnknownGroup()
                            .addProperty(new Property("org.rodnansol.unknown-type", "java.lang.String")),
                        new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
                        new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
                    ));
                    AsciiDocTemplateCustomization expectedTemplateCustomization = new AsciiDocTemplateCustomization();
                    expectedTemplateData.setTemplateCustomization(expectedTemplateCustomization);
                    expectedTemplateData.setMainDescription(TEST_DESCRIPTION);
                    verify(propertyGroupFilterService).postProcessPropertyGroups(new PostProcessPropertyGroupsCommand(expectedTemplateCustomization,propertyGroups, List.of("excluded-group"), List.of("included-group"),List.of("excluded-property"),List.of("included-property")));
                    verify(templateCompiler).compileTemplate(singleTemplate, expectedTemplateData);
                });
            });
        });
    }

    private CreateDocumentCommand getCreateDocumentCommand(SimpleProject project, File inputFile, File output, String singleTemplate) {
        CreateDocumentCommand command = new CreateDocumentCommand(project, TEST, inputFile, singleTemplate, output, new AsciiDocTemplateCustomization());
        command.setDescription(TEST_DESCRIPTION);
        command.setIncludedGroups(List.of("included-group"));
        command.setExcludedGroups(List.of("excluded-group"));
        command.setIncludedProperties(List.of("included-property"));
        command.setExcludedProperties(List.of("excluded-property"));
        return command;
    }
}

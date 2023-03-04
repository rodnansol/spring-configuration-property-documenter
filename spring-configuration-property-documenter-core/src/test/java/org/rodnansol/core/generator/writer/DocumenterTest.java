package org.rodnansol.core.generator.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.*;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.simple.SimpleProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        // Given
        SimpleProject project = ProjectFactory.ofSimpleProject(new File("."), TEST);
        File inputFile = new File("input-file");
        File output = tempDir.resolve("output-file").toFile();
        String singleTemplate = TemplateType.ADOC.getSingleTemplate();
        CreateDocumentCommand command = getCreateDocumentCommand(project, inputFile, output, singleTemplate);

        // When
        when(metadataInputResolverContext.getInputStreamFromFile(project, inputFile)).thenReturn(inputStream);
        List<PropertyGroup> propertyGroups = List.of(
            PropertyGroup.createUnknownGroup()
                .addProperty(new Property("org.rodnansol.unknown-type", "java.lang.String")),
            new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
            new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
        );
        when(metadataReader.readPropertiesAsPropertyGroupList(inputStream)).thenReturn(propertyGroups);
        when(templateCompiler.compileTemplate(eq(singleTemplate), any())).thenReturn("Hello World");
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        MainTemplateData expectedTemplateData = new MainTemplateData(TEST, List.of(
            PropertyGroup.createUnknownGroup()
                .addProperty(new Property("org.rodnansol.unknown-type", "java.lang.String")),
            new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
            new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
        ));
        expectedTemplateData.setTemplateCustomization(new AsciiDocTemplateCustomization());
        expectedTemplateData.setMainDescription(TEST_DESCRIPTION);
        verify(propertyGroupFilterService).filterPropertyGroups(propertyGroups, List.of(), List.of());
        verify(propertyGroupFilterService).filterPropertyGroupProperties(propertyGroups, List.of(), List.of());
        verify(propertyGroupFilterService).removeEmptyGroups(propertyGroups);
        verify(templateCompiler).compileTemplate(singleTemplate, expectedTemplateData);
    }

    @Test
    void readMetadataAndGenerateRenderedFile_shouldWriteContentToFileButSkipUnknownGroups_whenUnknownGroupIsIgnored() throws IOException {
        // Given
        SimpleProject project = ProjectFactory.ofSimpleProject(new File("."), TEST);
        File inputFile = new File("input-file");
        File output = tempDir.resolve("output-file").toFile();
        String singleTemplate = TemplateType.ADOC.getSingleTemplate();
        AsciiDocTemplateCustomization templateCustomization = new AsciiDocTemplateCustomization();
        templateCustomization.setIncludeUnknownGroup(false);
        CreateDocumentCommand command = new CreateDocumentCommand(project, TEST, inputFile, singleTemplate, output, templateCustomization);
        command.setDescription(TEST_DESCRIPTION);

        // When
        when(metadataInputResolverContext.getInputStreamFromFile(project, inputFile)).thenReturn(inputStream);
        when(metadataReader.readPropertiesAsPropertyGroupList(inputStream)).thenReturn(new ArrayList<>(List.of(
            PropertyGroup.createUnknownGroup()
                .addProperty(new Property("org.rodnansol.unknown-type", "java.lang.String")),
            new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
            new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
        )));
        when(templateCompiler.compileTemplate(eq(singleTemplate), any())).thenReturn("Hello World");
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        MainTemplateData expectedTemplateData = new MainTemplateData(TEST, List.of(
            new PropertyGroup("group1", "type", "sourceType", List.of(new Property("org.rodnansol.value", "java.lang.String"))),
            new PropertyGroup("group2", "type", "sourceType", List.of(new Property("org.rodnansol.another-value", "java.lang.String")))
        ));
        expectedTemplateData.setTemplateCustomization(templateCustomization);
        expectedTemplateData.setMainDescription(TEST_DESCRIPTION);
        verify(templateCompiler).compileTemplate(singleTemplate, expectedTemplateData);
    }

    private CreateDocumentCommand getCreateDocumentCommand(SimpleProject project, File inputFile, File output, String singleTemplate) {
        CreateDocumentCommand command = new CreateDocumentCommand(project, TEST, inputFile, singleTemplate, output, new AsciiDocTemplateCustomization());
        command.setDescription(TEST_DESCRIPTION);
        command.setIncludedGroups(List.of());
        command.setExcludedGroups(List.of());
        command.setIncludedProperties(List.of());
        command.setExcludedProperties(List.of());
        return command;
    }
}

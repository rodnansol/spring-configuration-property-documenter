package org.rodnansol.core.generator.writer;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.TemplateMode;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.customization.AbstractTemplateCustomization;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.generator.template.customization.HtmlTemplateCustomization;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;
import org.rodnansol.core.generator.template.customization.XmlTemplateCustomization;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.simple.SimpleProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class DocumenterIT {

    protected static final String TEST_DOCUMENT_HEADER = "IT";
    protected static final SimpleProject PROJECT = ProjectFactory.ofSimpleProject(new File("."), TEST_DOCUMENT_HEADER);
    protected static final String TEST_DESCRIPTION = "This is a test description";
    private static final String MAIN_EXPECTED_FOLDER = "src/it/resources/it/single/";
    private static final String TEST_JSON = MAIN_EXPECTED_FOLDER + "spring-configuration-metadata-empty-sourceType.json";
    @TempDir
    Path tempDir;

    Documenter underTest = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultCompiler(), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);

    public static Stream<TestCase> noEnvFormatCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.md"),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.adoc"),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.html")
//            new TestCase(TEST_JSON, TemplateType.XML, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.xml", false)
        );
    }

    public static Stream<TestCase> envFormatCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.md"),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.adoc"),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.html"),
            new TestCase(TEST_JSON, TemplateType.XML, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.xml")
        );
    }

    public static Stream<TestCase> compactModeCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-compact-mode.md"),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-compact-mode.adoc"),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-compact-mode.html")
        );
    }

    private AbstractTemplateCustomization getTemplateCustomization(TemplateType templateType) {
        AbstractTemplateCustomization templateCustomization = null;
        switch (templateType) {
            case MARKDOWN:
                templateCustomization = new MarkdownTemplateCustomization();
                break;
            case ADOC:
                templateCustomization = new AsciiDocTemplateCustomization();
                break;
            case HTML:
                templateCustomization = new HtmlTemplateCustomization();
                break;
            case XML:
                templateCustomization = new XmlTemplateCustomization();
                break;
            default:
                fail("Unknown template type");
                break;
        }
        templateCustomization.setIncludeGenerationDate(false);
        return templateCustomization;
    }

    @ParameterizedTest
    @MethodSource("noEnvFormatCases")
    @DisplayName("Should NOT include environment variable format when disabled")
    void readMetadataAndGenerateRenderedFile_shouldNotRenderEnvironmentFormatIntoFinalFile_whenEnvironmentFormatIsDisabled(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-without-env-format-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        templateCustomization.getContentCustomization().setIncludeEnvFormat(false);
        CreateDocumentCommand command = new CreateDocumentCommand(PROJECT, TEST_DOCUMENT_HEADER, new File(testCase.inputFile), templateType.getSingleTemplate(templateCustomization.getTemplateMode()), resolve.toFile(), templateCustomization);
        command.setDescription(TEST_DESCRIPTION);
        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        assertThat(resolve).hasSameTextualContentAs(Path.of(testCase.expectedFile));
    }

    @ParameterizedTest
    @MethodSource("envFormatCases")
    @DisplayName("Should include environment variable format when enabled")
    void readMetadataAndGenerateRenderedFile_shouldRenderEnvironmentFormatIntoFinalFile_whenEnvironmentFormatIsEnabled(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-with-env-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        templateCustomization.getContentCustomization().setIncludeEnvFormat(true);

        CreateDocumentCommand command = new CreateDocumentCommand(PROJECT, TEST_DOCUMENT_HEADER, new File(testCase.inputFile), templateType.getSingleTemplate(templateCustomization.getTemplateMode()), resolve.toFile(), templateCustomization);
        command.setDescription(TEST_DESCRIPTION);
        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        assertThat(resolve).hasSameTextualContentAs(Path.of(testCase.expectedFile));
    }

    @ParameterizedTest
    @MethodSource("compactModeCases")
    @DisplayName("Should render document in compact mode when compact mode is enabled")
    void readMetadataAndGenerateRenderedFile_shouldReturnDocumentInCompactMode_whenCompactModeIsEnabled(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-compact-mode-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        templateCustomization.setTemplateMode(TemplateMode.COMPACT);
        CreateDocumentCommand command = new CreateDocumentCommand(PROJECT, TEST_DOCUMENT_HEADER, new File(testCase.inputFile), templateType.getSingleTemplate(templateCustomization.getTemplateMode()), resolve.toFile(), templateCustomization);
        command.setDescription(TEST_DESCRIPTION);
        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        assertThat(resolve).hasSameTextualContentAs(Path.of(testCase.expectedFile));
    }

    @Test
    @DisplayName("Should return an empty file with basic content when the input file is missing and it is set to not fail")
    void readMetadataAndGenerateRenderedFile_shouldReturnEmptyFile_whenInputFileIsMissing() throws IOException {
        // Given
        TemplateType templateType = TemplateType.ADOC;
        Path resolve = tempDir.resolve("IT-output-non-existing-source-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        CreateDocumentCommand command = new CreateDocumentCommand(PROJECT, TEST_DOCUMENT_HEADER, new File("non-existing-file.json"), templateType.getSingleTemplate(templateCustomization.getTemplateMode()), resolve.toFile(), templateCustomization);
        command.setFailOnMissingInput(false);
        command.setDescription(TEST_DESCRIPTION);

        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        assertThat(resolve).hasSameTextualContentAs(Path.of(MAIN_EXPECTED_FOLDER + "expected-non-existing-input-file.adoc"));
    }

    @Test
    @DisplayName("Should throw DocumentGenerationException exception when the input file is missing and it is meant to fail in this case")
    void readMetadataAndGenerateRenderedFile_shouldFail_whenInputFileIsMissingAndItIsMeanToFail() {
        // Given
        TemplateType templateType = TemplateType.ADOC;
        Path resolve = tempDir.resolve("IT-output-non-existing-source-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        CreateDocumentCommand command = new CreateDocumentCommand(PROJECT, TEST_DOCUMENT_HEADER, new File("non-existing-file.json"), templateType.getSingleTemplate(templateCustomization.getTemplateMode()), resolve.toFile(), templateCustomization);
        command.setFailOnMissingInput(true);
        command.setDescription(TEST_DESCRIPTION);

        // When
        ThrowingCallable callable = () -> underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        assertThatThrownBy(callable).isInstanceOf(DocumentGenerationException.class);
    }

    static class TestCase {

        final String inputFile;
        final TemplateType templateType;
        final String expectedFile;

        public TestCase(String inputFile, TemplateType templateType, String expectedFile) {
            this.inputFile = inputFile;
            this.templateType = templateType;
            this.expectedFile = expectedFile;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                " templateType=" + templateType +
                ", expectedFile='" + expectedFile + '\'' +
                ", inputFile='" + inputFile + '\'' +
                '}';
        }
    }
}

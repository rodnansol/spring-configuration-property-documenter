package org.rodnansol.core.generator.writer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class AggregationDocumenterIT {

    protected static final SimpleProject PROJECT = ProjectFactory.ofSimpleProject(new File("."), "IT");
    private static final String MAIN_EXPECTED_FOLDER = "src/it/resources/it/aggregated/";
    private static final String TEST_JSON = MAIN_EXPECTED_FOLDER + "spring-configuration-metadata-empty-sourceType.json";

    @TempDir
    Path tempDir;

    AggregationDocumenter underTest = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultProvidedInstance(), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);

    public static Stream<TestCase> noEnvFormatCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.md"),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.adoc"),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.html")
        );
    }

    public static Stream<TestCase> envFormatCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.md"),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.adoc"),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.html")
        );
    }

    @ParameterizedTest
    @MethodSource("noEnvFormatCases")
    void readMetadataAndGenerateRenderedFile_shouldNotRenderEnvironmentFormatIntoFinalFile_whenEnvironmentFormatIsDisabled(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-aggregated-without-env-format-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        templateCustomization.getContentCustomization().setIncludeEnvFormat(false);
        CreateAggregationCommand command = getCreateAggregationCommand(testCase, templateType, resolve, templateCustomization);

        // When
        underTest.createDocumentsAndAggregate(command);

        // Then
        List<String> actualFile = Files.readAllLines(resolve);
        List<String> expectedFile = Files.readAllLines(Path.of(testCase.expectedFile));
        assertThat(actualFile).containsExactlyElementsOf(expectedFile);
    }

    @ParameterizedTest
    @MethodSource("envFormatCases")
    void readMetadataAndGenerateRenderedFile_shouldRenderEnvironmentFormatIntoFinalFile_whenEnvironmentFormatIsEnabled(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-aggregated-with-env-format-" + templateType.name() + templateType.getExtension());
        AbstractTemplateCustomization templateCustomization = getTemplateCustomization(templateType);
        templateCustomization.getContentCustomization().setIncludeEnvFormat(true);
        CreateAggregationCommand command = getCreateAggregationCommand(testCase, templateType, resolve, templateCustomization);

        // When
        underTest.createDocumentsAndAggregate(command);

        // Then
        List<String> actualFile = Files.readAllLines(resolve);
        List<String> expectedFile = Files.readAllLines(Path.of(testCase.expectedFile));
        assertThat(actualFile).containsExactlyElementsOf(expectedFile);
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

    private CreateAggregationCommand getCreateAggregationCommand(TestCase testCase, TemplateType templateType, Path resolve, AbstractTemplateCustomization templateCustomization) {
        return new CreateAggregationCommand(PROJECT, "IT",
            List.of(
                new CombinedInput(new File(testCase.inputFile), "Document 1"),
                new CombinedInput(new File(testCase.inputFile), "Document 2")
            ),
            templateType, templateCustomization, resolve.toFile());
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
                "templateType=" + templateType +
                ", expectedFile='" + expectedFile + '\'' +
                ", inputFile='" + inputFile + '\'' +
                '}';
        }
    }
}

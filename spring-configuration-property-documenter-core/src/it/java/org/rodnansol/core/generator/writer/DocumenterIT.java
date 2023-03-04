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
class DocumenterIT {

    private static final String MAIN_EXPECTED_FOLDER = "src/it/resources/it/single/";
    private static final String TEST_JSON = MAIN_EXPECTED_FOLDER + "spring-configuration-metadata-empty-sourceType.json";

    @TempDir
    Path tempDir;

    Documenter underTest = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultProvidedInstance(), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);

    public static Stream<TestCase> testCases() {
        return Stream.of(
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.md", true),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.adoc", true),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.html", true),
            new TestCase(TEST_JSON, TemplateType.XML, MAIN_EXPECTED_FOLDER + "expected-with-env-variables.xml", true),
            new TestCase(TEST_JSON, TemplateType.MARKDOWN, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.md", false),
            new TestCase(TEST_JSON, TemplateType.ADOC, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.adoc", false),
            new TestCase(TEST_JSON, TemplateType.HTML, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.html", false)
//            new TestCase(TEST_JSON, TemplateType.XML, MAIN_EXPECTED_FOLDER + "expected-without-env-variables.xml", false)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void readMetadataAndGenerateRenderedFile_shouldReadMetadataFileAndCreateRenderedDocument(TestCase testCase) throws IOException {
        // Given
        TemplateType templateType = testCase.templateType;
        Path resolve = tempDir.resolve("IT-output-" + testCase.includeEnvs + "-" + templateType.name() + templateType.getExtension());
        SimpleProject project = ProjectFactory.ofSimpleProject(new File("."), "IT");
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
        templateCustomization.setIncludeEnvFormat(testCase.includeEnvs);
        templateCustomization.setIncludeGenerationDate(false);
        CreateDocumentCommand command = new CreateDocumentCommand(project, "IT", new File(testCase.inputFile), templateType.getSingleTemplate(), resolve.toFile(), templateCustomization);

        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        List<String> actualFile = Files.readAllLines(resolve);
        List<String> expectedFile = Files.readAllLines(Path.of(testCase.expectedFile));
        assertThat(actualFile).containsExactlyElementsOf(expectedFile);
    }

    static class TestCase {

        final String inputFile;
        final TemplateType templateType;
        final String expectedFile;

        final boolean includeEnvs;

        public TestCase(String inputFile, TemplateType templateType, String expectedFile, boolean includeEnvs) {
            this.inputFile = inputFile;
            this.templateType = templateType;
            this.expectedFile = expectedFile;
            this.includeEnvs = includeEnvs;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                "inputFile='" + inputFile + '\'' +
                ", templateType=" + templateType +
                ", expectedFile='" + expectedFile + '\'' +
                ", includeEnvs=" + includeEnvs +
                '}';
        }
    }
}

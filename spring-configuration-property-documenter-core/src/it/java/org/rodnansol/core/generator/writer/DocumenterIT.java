package org.rodnansol.core.generator.writer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.simple.SimpleProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DocumenterIT {

    @TempDir
    Path tempDir;

    Documenter underTest = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultProvidedInstance(), MetadataInputResolverContext.INSTANCE);

    public static Stream<TestCase> testCases() {
        return Stream.of(
            new TestCase("src/it/resources/it/spring-configuration-metadata-empty-sourceType.json","src/it/resources/it/expected-spring-configuration-metadata-empty-sourceType.adoc")
        );
    }

    private void removeLastLines(List<String> lines, int linesToRemove) {
        for (int i = 0; i < linesToRemove; i++) {
            lines.remove(lines.size() - 1);
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void readMetadataAndGenerateRenderedFile_shouldReadMetadataFileAndCreateRenderedDocument(TestCase testCase) throws IOException {
        // Given
        Path resolve = tempDir.resolve("IT-output.adoc");
        SimpleProject project = ProjectFactory.ofSimpleProject(new File("."), "IT");
        CreateDocumentCommand command = new CreateDocumentCommand(project, "IT", new File(testCase.inputFile), TemplateType.ADOC.getSingleTemplate(), resolve.toFile(), new AsciiDocTemplateCustomization());

        // When
        underTest.readMetadataAndGenerateRenderedFile(command);

        // Then
        List<String> actualFile = Files.readAllLines(resolve);
        removeLastLines(actualFile, 3);
        List<String> expectedFile = Files.readAllLines(Path.of(testCase.expectedFile));
        removeLastLines(expectedFile, 3);
        assertThat(actualFile).containsExactlyElementsOf(expectedFile);
    }

    static class TestCase {
        final String inputFile;
        final String expectedFile;

        public TestCase(String inputFile, String expectedFile) {
            this.inputFile = inputFile;
            this.expectedFile = expectedFile;
        }
    }
}

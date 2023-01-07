package org.rodnansol.core.generator.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileMetadataInputResolverTest {

    protected static final String CONTENT_IN_FILE = "Hello World";
    @TempDir
    Path tempDir;

    private final FileMetadataInputResolver underTest = new FileMetadataInputResolver();

    @Test
    void resolveInputStream_shouldReturnFileInputStream_whenFileExists() throws IOException {
        // Given
        Path tempFilePath = tempDir.resolve("temp-file.txt");
        Files.writeString(tempFilePath, CONTENT_IN_FILE);

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempFilePath.toFile());

        // Then
        assertThat(inputStream).hasContent(CONTENT_IN_FILE);
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenTheFileDoesNotExist() throws IOException {
        // Given
        Path tempFilePath = tempDir.resolve("temp-file.txt");

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempFilePath.toFile());

        // When
        assertThat(inputStream).isNull();
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenTheInputIsADirectory() {
        // Given

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempDir.toFile());

        // When
        assertThat(inputStream).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"file.zip", "file.jar"})
    void supports_shouldReturnFalse_whenTheInputFileIsZipOrJar(String fileName) {
        // Given
        // When
        boolean supports = underTest.supports(new File(fileName));
        // Then
        assertThat(supports).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"file.txt", "file.xml"})
    void supports_shouldReturnTrue_whenTheInputFileIsNotZipOrJar(String fileName) throws IOException {
        // Given
        Path resolve = tempDir.resolve(fileName);
        Files.writeString(resolve,"Hello World");
        // When
        boolean supports = underTest.supports(resolve.toFile());
        // Then
        assertThat(supports).isTrue();
    }
}

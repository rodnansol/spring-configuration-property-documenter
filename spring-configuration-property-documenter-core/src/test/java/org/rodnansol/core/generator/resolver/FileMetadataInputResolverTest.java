package org.rodnansol.core.generator.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rodnansol.core.project.ProjectFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileMetadataInputResolverTest {

    protected static final String CONTENT_IN_FILE = "Hello World";
    private FileMetadataInputResolver underTest = new FileMetadataInputResolver();


    @Test
    void resolveInputStream_shouldReturnFileInputStream_whenFileExists(@TempDir Path tempDir) throws IOException {
        // Given
        Path tempFilePath = tempDir.resolve("temp-file.txt");
        Files.writeString(tempFilePath, CONTENT_IN_FILE);

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempFilePath.toFile());

        // Then
        assertThat(inputStream).hasContent(CONTENT_IN_FILE);
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenTheFileDoesNotExist(@TempDir Path tempDir) throws IOException {
        // Given
        Path tempFilePath = tempDir.resolve("temp-file.txt");

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempFilePath.toFile());

        // When
        assertThat(inputStream).isNull();
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenTheInputIsADirectory(@TempDir Path tempDir) {
        // Given

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempDir.toFile());

        // When
        assertThat(inputStream).isNull();
    }
}

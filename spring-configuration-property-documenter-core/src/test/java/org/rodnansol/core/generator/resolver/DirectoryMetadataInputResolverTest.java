package org.rodnansol.core.generator.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.util.CoreFileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DirectoryMetadataInputResolverTest {

    protected static final String CONTENT_IN_FILE = "Hello World";

    @TempDir
    Path tempDir;

    private DirectoryMetadataInputResolver underTest = new DirectoryMetadataInputResolver();

    static Stream<Arguments> getMavenFolders() {
        return ProjectFactory.ofMavenProject(new File(""), "Maven")
            .getPossibleMetadataFilePaths()
            .stream()
            .map(Arguments::of);
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenTheDirectoryDoesNotTheRequiredFile() {
        // Given
        Path directory = tempDir.resolve("directory");

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), directory.toFile());

        // Then
        assertThat(inputStream).isNull();
    }

    @ParameterizedTest
    @MethodSource("getMavenFolders")
    void resolveInputStream_shouldReturnTheMetadaFile_whenTheDirectoryContainsIt(String path) throws IOException {
        // Given
        Path targetPath = tempDir.resolve(path.substring(1));
        CoreFileUtils.initializeFileWithPath(targetPath.toFile());
        Files.writeString(targetPath, CONTENT_IN_FILE);

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempDir.toFile());

        // Then
        assertThat(inputStream).hasContent(CONTENT_IN_FILE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"file", "anotherFile"})
    void supports_shouldReturnFalse_whenInputIsNotDirectory(String input) throws IOException {
        // Given
        Path resolve = tempDir.resolve(input);
        Files.writeString(resolve, "Hello World");
        // When
        boolean supports = underTest.supports(resolve.toFile());
        // Then
        assertThat(supports).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"target", "META-INF"})
    void supports_shouldReturnTrue_whenTheInputFileIsNotZipOrJar(String input) throws IOException {
        // Given
        Path resolve = tempDir.resolve(input);
        resolve.toFile().mkdirs();
        // When
        boolean supports = underTest.supports(resolve.toFile());
        // Then
        assertThat(supports).isTrue();
    }

}

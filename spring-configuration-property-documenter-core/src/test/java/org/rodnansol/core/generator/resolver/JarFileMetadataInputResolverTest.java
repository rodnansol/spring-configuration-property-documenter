package org.rodnansol.core.generator.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.ProjectFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class JarFileMetadataInputResolverTest {

    protected static final String CONTENT_IN_FILE = "Hello World";
    private JarFileMetadataInputResolver underTest = new JarFileMetadataInputResolver();


    @Test
    void resolveInputStream_shouldReturnInputStream_whenInputJarFileContainsTheEntry(@TempDir Path tempDir) throws IOException {
        // Given
        Path jarFilePath = tempDir.resolve("temp.jar");
        File tempJar = jarFilePath.toFile();
        createJarFile(tempJar, JarFileMetadataInputResolver.PATH_IN_JAR_FILE);

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempJar);

        // Then
        assertThat(inputStream).hasContent(CONTENT_IN_FILE);
    }

    @Test
    void resolveInputStream_shouldReturnNull_whenInputJarFileDoesNotContainTheEntry(@TempDir Path tempDir) throws IOException {
        // Given
        Path jarFilePath = tempDir.resolve("temp.jar");
        File tempJar = jarFilePath.toFile();
        createJarFile(tempJar, "not-the-file-you-are-looking.for");

        // When
        InputStream inputStream = underTest.resolveInputStream(ProjectFactory.ofMavenProject(tempDir.toFile(), "Maven"), tempJar);

        // Then
        assertThat(inputStream).isNull();
    }

    private void createJarFile(File tempJar, String pathInJarFile) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempJar), manifest);
        JarEntry entry = new JarEntry(pathInJarFile);
        entry.setTime(new File(pathInJarFile).lastModified());
        jarOutputStream.putNextEntry(entry);
        jarOutputStream.write(CONTENT_IN_FILE.getBytes());
        jarOutputStream.closeEntry();
        jarOutputStream.close();
    }
}

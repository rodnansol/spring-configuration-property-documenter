package org.rodnansol.core.generator.template.handlebars;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkingDirectoryAwareRecursiveFileTemplateLoaderTest {

    @Mock
    WorkingDirectoryProvider workingDirectoryProvider;

    @TempDir
    Path tempDir;

    static Stream<Arguments> getFinalLocationArgs() {
        return Stream.of(
            Arguments.of("home/project/multi-module/multi-module-docs/src/main/resources/templates/content-1.md.hbs","/home/project", "multi-module/multi-module-docs/src/main/resources/templates/content-1.md.hbs"),
            Arguments.of("home/project/multi-module/multi-module-docs/src/main/resources/templates/content-2.md.hbs","/home/project", "src/main/resources/templates/content-2.md.hbs"),
            Arguments.of("home/project/multi-module/multi-module-docs/src/main/resources/templates/content-3.md.hbs","/home/project/multi-module/multi-module-docs", "multi-module/multi-module-docs/src/main/resources/templates/content-3.md.hbs"),
            Arguments.of("home/project/multi-module/multi-module-docs/src/main/resources/templates/content-4.md.hbs","/home/project/multi-module/multi-module-docs", "src/main/resources/templates/content-4.md.hbs")
        );
    }

    @ParameterizedTest
    @MethodSource("getFinalLocationArgs")
    void getFinalLocationWithWorkingDirectoryRecursiveStep_shouldReturnResource_whenTheLocationIsOverlappedByTheWorkingDirectory(String contentLocation, String workingDirectory, String resourceLocation) throws IOException {
        // Given
        Path resolve = tempDir.resolve(contentLocation);
        File tempDirectory = resolve.toFile().getParentFile();
        tempDirectory.mkdirs();

        resolve.toFile().createNewFile();
        Files.writeString(resolve, "Hello World");

        String tempWorkingDirectory = tempDir + workingDirectory;
        when(workingDirectoryProvider.getCurrentWorkingDirectoryPath()).thenReturn(Path.of(tempWorkingDirectory));

        // When
        WorkingDirectoryAwareRecursiveFileTemplateLoader underTest = new WorkingDirectoryAwareRecursiveFileTemplateLoader(tempWorkingDirectory, workingDirectoryProvider);
        URL resource = underTest.getResource(resourceLocation);

        // Then
        InputStream inputStream = resource.openStream();
        assertThat(inputStream)
            .hasContent("Hello World");
    }
}

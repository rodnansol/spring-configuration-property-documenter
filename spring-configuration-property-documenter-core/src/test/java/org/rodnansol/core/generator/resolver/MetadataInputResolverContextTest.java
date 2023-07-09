package org.rodnansol.core.generator.resolver;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.simple.SimpleProject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataInputResolverContextTest {

    @Test
    void getInputStreamFromFile_shouldThrowException_whenResolverDoesNotSupportAndStrategyIsThrowException() {
        // Given
        File file = new File("test.json");
        MetadataInputResolver metadataInputResolver = mock(MetadataInputResolver.class);
        when(metadataInputResolver.supports(file)).thenReturn(false);
        // When
        MetadataInputResolverContext underTest = new MetadataInputResolverContext(List.of(metadataInputResolver));
        ThrowingCallable throwable = () -> underTest.getInputStreamFromFile(new SimpleProject(new File("."), "test-folder"),
            file, InputFileResolutionStrategy.THROW_EXCEPTION);
        // Then
        assertThatThrownBy(throwable).isInstanceOf(DocumentGenerationException.class);
    }

    @Test
    void getInputStreamFromFile_shouldReturnNullStream_whenResolverDoesNotSupportAndStrategyIsReturnEmpty() throws IOException {
        // Given
        File file = new File("test.json");
        MetadataInputResolver metadataInputResolver = mock(MetadataInputResolver.class);
        when(metadataInputResolver.supports(file)).thenReturn(false);
        // When
        MetadataInputResolverContext underTest = new MetadataInputResolverContext(List.of(metadataInputResolver));
        InputStream inputStream = underTest.getInputStreamFromFile(new SimpleProject(new File("."), "test-folder"),
            file, InputFileResolutionStrategy.RETURN_EMPTY);

        // Then
        assertThat(inputStream).isNotNull();
        assertThat(inputStream.read()).isEqualTo(-1);
    }

    @Test
    void getInputStreamFromFile_shouldReturnInputStreamContainingTheFileContent_whenResolverSupports() throws IOException {
        // Given
        File inputFile = new File("test.json");
        SimpleProject simpleProject = new SimpleProject(new File("."), "test-folder");
        MetadataInputResolver metadataInputResolver = mock(MetadataInputResolver.class);
        when(metadataInputResolver.supports(inputFile)).thenReturn(true);
        when(metadataInputResolver.resolveInputStream(simpleProject, inputFile))
            .thenReturn(new ByteArrayInputStream("Hello World".getBytes()));

        // When
        MetadataInputResolverContext underTest = new MetadataInputResolverContext(List.of(metadataInputResolver));
        InputStream inputStream = underTest.getInputStreamFromFile(simpleProject,
            inputFile, InputFileResolutionStrategy.RETURN_EMPTY);

        // Then
        assertThat(inputStream).hasContent("Hello World");
    }
}
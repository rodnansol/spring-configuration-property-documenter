package org.rodnansol.core.generator.template.compiler;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerFactory;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateCompilerFactoryTest {

    static Stream<Arguments> validClassNameSource() {
        return Stream.of(
            Arguments.of("org.rodnansol.core.generator.template.compiler.TestCompiler", "TestCompiler Template"),
            Arguments.of("org.rodnansol.core.generator.template.compiler.AnotherTestCompiler", "Another Test Compiler Template")
        );
    }

    @ParameterizedTest
    @MethodSource("validClassNameSource")
    void getInstance_shouldReturnTemplateCompilerInstance_whenInstanceExists(String className, String expectedCompiledTemplate) {
        // Given
        // When
        TemplateCompiler instance = TemplateCompilerFactory.getInstance(className);
        // Then
        assertThat(instance.compileTemplate(null, null)).isEqualTo(expectedCompiledTemplate);
    }

    @Test
    void getInstance_shouldThrowException_whenInstanceDoesNotExist() {
        // Given
        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> TemplateCompilerFactory.getInstance("DoesNotExist"))
            .isInstanceOf(IllegalStateException.class);
        // Then
    }

}

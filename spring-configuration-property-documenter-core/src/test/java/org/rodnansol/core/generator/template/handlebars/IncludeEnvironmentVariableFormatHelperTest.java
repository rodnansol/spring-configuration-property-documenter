package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.template.data.MainTemplateData;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IncludeEnvironmentVariableFormatHelperTest {

    IncludeEnvironmentVariableFormatHelper underTest = new IncludeEnvironmentVariableFormatHelper();

    @Test
    void apply_shouldCallFn_whenTheTemplateShouldIncludeEnvFormat() throws IOException {
        // Given
        MarkdownTemplateCustomization templateCustomization = new MarkdownTemplateCustomization();
        MainTemplateData mainTemplateData = new MainTemplateData("Test", List.of());
        templateCustomization.setIncludeEnvFormat(true);
        mainTemplateData.setTemplateCustomization(templateCustomization);
        Options options = mock(Options.class);

        // When
        underTest.apply(mainTemplateData, options);

        // Then
        verify(options).fn(mainTemplateData);
    }

    @Test
    void apply_shouldCallInverse_whenTheTemplateShouldNotIncludeEnvFormat() throws IOException {
        // Given
        MarkdownTemplateCustomization templateCustomization = new MarkdownTemplateCustomization();
        MainTemplateData mainTemplateData = new MainTemplateData("Test", List.of());
        templateCustomization.setIncludeEnvFormat(false);
        mainTemplateData.setTemplateCustomization(templateCustomization);
        Options options = mock(Options.class);

        // When
        underTest.apply(mainTemplateData, options);

        // Then
        verify(options).inverse(mainTemplateData);
    }
}

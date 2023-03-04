package org.rodnansol.core.generator.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.I18nHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlebarsTemplateCompilerTest {

    protected static final String TEMPLATE = "template";
    protected static final String FINAL_DOCUMENT = "Final document";

    @Mock
    I18nHelper i18nHelper;

    @Mock
    Template template;

    @Mock
    Handlebars handlebars;

    @InjectMocks
    HandlebarsTemplateCompiler underTest;

    @Test
    void compileTemplate_shouldSetLocaleAndApplyContext() throws IOException {
        // Given
        Locale locale = Locale.forLanguageTag("hu");
        AsciiDocTemplateCustomization templateCustomization = new AsciiDocTemplateCustomization();
        templateCustomization.setLocale("hu");
        MainTemplateData templateData = new MainTemplateData("Test", List.of());
        templateData.setTemplateCustomization(templateCustomization);
        when(handlebars.compile(TEMPLATE)).thenReturn(template);
        when(handlebars.<String>helper("i18n")).thenReturn(i18nHelper);
        when(template.apply(templateData)).thenReturn(FINAL_DOCUMENT);

        // When
        String output = underTest.compileTemplate(TEMPLATE, templateData);

        // Then
        assertThat(output).isEqualTo(FINAL_DOCUMENT);
        verify(handlebars).compile(TEMPLATE);
        verify(i18nHelper).setDefaultLocale(locale);
    }
}

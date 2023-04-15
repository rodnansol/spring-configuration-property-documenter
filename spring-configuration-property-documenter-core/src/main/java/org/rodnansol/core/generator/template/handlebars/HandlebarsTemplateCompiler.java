package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.I18nHelper;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.data.TemplateData;
import org.rodnansol.core.generator.template.compiler.ThreadLocalTemplateCompilerStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * Template compiler implementation that uses <a href="https://jknack.github.io/handlebars.java"/>Handlebars</a>to compile a template.
 * <p>
 * Implementation is using custom helpers:
 * <ul>
 *     <li>as_env - Converts the property key to its environment variable format.</li>
 *     <li>is_included - Checks if the given parameters should be rendered in the final document or not.</li>
 *     <li>is_compact_mode - Checks if the incoming template customization has the compact mode enabled or not.</li>
 * </ul>
 *
 * @author nandorholozsnyak
 * @since 0.1.2
 */
public class HandlebarsTemplateCompiler implements TemplateCompiler {

    public static final TemplateCompiler INSTANCE = new HandlebarsTemplateCompiler();

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsTemplateCompiler.class);
    private static final String HELPER_AS_ENV = "as_env";
    private static final String HELPER_IS_INCLUDED = "is_included";
    private static final String HELPER_IS_COMPACT_MODE = "is_compact_mode";
    private final Handlebars handlebars;
    private final I18nHelper i18nHelper;

    public HandlebarsTemplateCompiler(Handlebars handlebars, I18nHelper i18nHelper) {
        this.handlebars = handlebars;
        this.i18nHelper = i18nHelper;
    }

    public HandlebarsTemplateCompiler() {
        this(createDefaultHandlebarsInstance(), I18nHelper.i18n);
    }

    private static Handlebars createDefaultHandlebarsInstance() {
        return new Handlebars()
            .registerHelper(HELPER_AS_ENV, new EnvironmentVariableHelper())
            .registerHelper(HELPER_IS_INCLUDED, new IsIncludedHelper(ThreadLocalTemplateCompilerStore.INSTANCE))
            .registerHelper(HELPER_IS_COMPACT_MODE, new IsCompactModeHelper(ThreadLocalTemplateCompilerStore.INSTANCE))
            .with(new ClassPathTemplateLoader(), new WorkingDirectoryAwareRecursiveFileTemplateLoader(".", WorkingDirectoryProvider.INSTANCE));
    }

    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        LOGGER.debug("Compiling template:[{}] with data:[{}]", templatePath, templatePath);
        Objects.requireNonNull(templatePath, "templatePath is NULL");
        Objects.requireNonNull(templateData, "templateData is NULL");
        try {
            setLocalizationIfPossible(templateData);
            return handlebars
                .compile(templatePath)
                .apply(templateData);
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during compiling the template with Handlebars engine", e);
        }
    }

    private void setLocalizationIfPossible(TemplateData templateData) {
        try {
            if (templateData.getTemplateCustomization() != null && templateData.getTemplateCustomization().getLocale() != null) {
                String locale = templateData.getTemplateCustomization().getLocale();
                LOGGER.debug("Setting locale to: [{}]", locale);
                i18nHelper.setDefaultLocale(Locale.forLanguageTag(locale));
            }
        } catch (Exception e) {
            LOGGER.warn("Error during setting the Localization for the documents, please check the logs for more information.");
        }
    }

}

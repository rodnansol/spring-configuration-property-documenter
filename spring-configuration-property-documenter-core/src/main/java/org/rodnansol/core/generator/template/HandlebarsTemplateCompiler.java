package org.rodnansol.core.generator.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.helper.I18nHelper;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.handlebars.EnvironmentVariableHelper;
import org.rodnansol.core.generator.template.handlebars.IncludeEnvironmentVariableFormatHelper;
import org.rodnansol.core.generator.template.handlebars.WorkingDirectoryAwareRecursiveFileTemplateLoader;
import org.rodnansol.core.generator.template.handlebars.WorkingDirectoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * Template compiler implementation that uses <a href="https://jknack.github.io/handlebars.java"/>Handlebars</a>to compile a template.
 *
 * @author nandorholozsnyak
 * @since 0.1.2
 */
public class HandlebarsTemplateCompiler implements TemplateCompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsTemplateCompiler.class);

    private final Handlebars handlebars;

    public HandlebarsTemplateCompiler(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    public HandlebarsTemplateCompiler() {
        this(createDefaultHandlebarsInstance());
    }

    private static Handlebars createDefaultHandlebarsInstance() {
        return new Handlebars()
            .registerHelper("as_env", new EnvironmentVariableHelper())
            .registerHelper("include_env_format", new IncludeEnvironmentVariableFormatHelper())
            .with(new ClassPathTemplateLoader(), new WorkingDirectoryAwareRecursiveFileTemplateLoader(".", WorkingDirectoryProvider.INSTANCE));
    }

    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        LOGGER.debug("Compiling template:[{}] with data:[{}]", templatePath, templatePath);
        Objects.requireNonNull(templatePath,"templatePath is NULL");
        Objects.requireNonNull(templateData,"templateData is NULL");
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
            if(templateData.getTemplateCustomization() != null && templateData.getTemplateCustomization().getLocale() != null) {
                Helper<String> i18n = handlebars.helper("i18n");
                if(i18n instanceof I18nHelper) {
                    String locale = templateData.getTemplateCustomization().getLocale();
                    LOGGER.debug("Setting locale to: [{}]", locale);
                    ((I18nHelper) i18n).setDefaultLocale(Locale.forLanguageTag(locale));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error during setting the Localization for the documents, please check the logs for more information.");
        }
    }

}

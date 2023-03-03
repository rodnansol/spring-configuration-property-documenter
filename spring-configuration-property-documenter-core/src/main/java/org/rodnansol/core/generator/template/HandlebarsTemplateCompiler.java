package org.rodnansol.core.generator.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.handlebars.EnvironmentVariableHelper;
import org.rodnansol.core.generator.template.handlebars.IncludeEnvironmentVariableFormatHelper;
import org.rodnansol.core.generator.template.handlebars.WorkingDirectoryAwareRecursiveFileTemplateLoader;
import org.rodnansol.core.generator.template.handlebars.WorkingDirectoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        try {
            return handlebars
                .compile(templatePath)
                .apply(templateData);
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during compiling the template with Handlebars engine", e);
        }
    }

}

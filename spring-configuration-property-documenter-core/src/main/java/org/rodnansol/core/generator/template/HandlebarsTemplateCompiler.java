package org.rodnansol.core.generator.template;

import com.github.jknack.handlebars.Handlebars;
import org.rodnansol.core.generator.DocumentGenerationException;
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

    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        LOGGER.debug("Compiling template:[{}] with data:[{}]", templatePath, templatePath);
        try {
            return new Handlebars()
                .compile(templatePath)
                .apply(templateData);
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during compiling the template with Handlebars engine", e);
        }
    }

}

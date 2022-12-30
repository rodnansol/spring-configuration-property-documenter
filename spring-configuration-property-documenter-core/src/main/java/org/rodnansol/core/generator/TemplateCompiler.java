package org.rodnansol.core.generator;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class that handles the template compiling.
 *
 * @author nandorholozsnyak
 * @since 0.0.1
 */
public class TemplateCompiler {

    public static final TemplateCompiler INSTANCE = new TemplateCompiler();

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateCompiler.class);

    private TemplateCompiler() {
    }

    /**
     * @param template
     * @param templateData
     * @return
     * @throws DocumentGenerationException
     */
    public String compileTemplate(String template, TemplateData templateData) throws DocumentGenerationException {
        LOGGER.debug("Compiling template:[{}] with data:[{}]", template, template);
        try {
            Template temp = new Handlebars().compile(template);
            return temp.apply(templateData);
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during compiling the template", e);
        }
    }

}

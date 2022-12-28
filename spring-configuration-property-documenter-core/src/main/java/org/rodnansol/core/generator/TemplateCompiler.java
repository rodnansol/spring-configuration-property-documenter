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

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateCompiler.class);

    public String compileTemplate(String template, TemplateData templateData) throws IOException {
        LOGGER.debug("Compiling template:[{}] with data:[{}]", template, template);
        Template temp = new Handlebars().compile(template);
        return temp.apply(templateData);
    }

}

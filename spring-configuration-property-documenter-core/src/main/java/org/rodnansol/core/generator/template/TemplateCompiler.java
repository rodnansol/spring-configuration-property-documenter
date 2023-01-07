package org.rodnansol.core.generator.template;

import org.rodnansol.core.generator.DocumentGenerationException;

/**
 * Class that handles the template compiling.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public interface TemplateCompiler {

    /**
     * Compiles the template with the given template data.
     * <p>
     * If the template is not found an exception  must be raised.
     *
     * @param templatePath path to the template that the compiler can understand and work with.
     * @param templateData template data.
     * @return templated populated and compiled with the given template data.
     * @throws DocumentGenerationException when the template compiler occurs an error.
     */
    String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException;
}

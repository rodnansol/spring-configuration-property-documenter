package org.rodnansol.core.generator.template.compiler;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.data.TemplateData;

/**
 * Class that handles the template compiling.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
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

    /**
     * Returns the associated memory store.
     *
     * @since 0.6.0
     */
    @NonNull
    default TemplateCompilerMemoryStore getMemoryStore() {
        return ThreadLocalTemplateCompilerStore.INSTANCE;
    }
}

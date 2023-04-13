package org.rodnansol.core.generator.template.compiler;

import org.rodnansol.core.generator.template.handlebars.HandlebarsTemplateCompiler;

import java.util.ServiceLoader;

/**
 * Factory class that uses the {@link ServiceLoader} class to have dynamic implementations for the {@link TemplateCompiler} and make sure end users are able to customize it for their own taste.
 *
 * @author nandorholozsnyak
 * @since 0.1.2
 */
public class TemplateCompilerFactory {

    private TemplateCompilerFactory() {
    }

    /**
     * Returns a {@link TemplateCompiler} instance by its class name.
     *
     * @param templateCompilerName template compiler's class name.
     * @return {@link TemplateCompiler} instance.
     * @throws IllegalStateException if the given template compiler name is not having any implementation.
     */
    public static TemplateCompiler getInstance(String templateCompilerName) {
        ServiceLoader<TemplateCompiler> load = ServiceLoader.load(TemplateCompiler.class);
        return load.stream()
            .filter(templateCompilerProvider -> templateCompilerProvider.type().getName().equals(templateCompilerName))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Unable to find TemplateCompiler implementation with name:[" + templateCompilerName + "]"))
            .get();
    }

    /**
     * Instantiates the incoming class by its name.
     *
     * @param className class to be created.
     * @return created class.
     * @throws IllegalStateException if the class can not be created.
     */
    public static TemplateCompiler getInstanceByClassName(String className) {
        try {
            return (TemplateCompiler) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to construct class:[" + className + "]", e);
        }
    }

    /**
     * Returns the default provided instance which is the {@link HandlebarsTemplateCompiler}.
     *
     * @return returns a new {@link HandlebarsTemplateCompiler} instance.
     */
    public static TemplateCompiler getDefaultProvidedInstance() {
        return new HandlebarsTemplateCompiler();
    }
}

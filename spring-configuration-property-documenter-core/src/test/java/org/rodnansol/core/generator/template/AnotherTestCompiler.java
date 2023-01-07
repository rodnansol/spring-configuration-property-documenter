package org.rodnansol.core.generator.template;

import org.rodnansol.core.generator.DocumentGenerationException;

public class AnotherTestCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "Another Test Compiler Template";
    }
}

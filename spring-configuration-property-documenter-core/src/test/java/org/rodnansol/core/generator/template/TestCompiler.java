package org.rodnansol.core.generator.template;

import org.rodnansol.core.generator.DocumentGenerationException;

public class TestCompiler implements TemplateCompiler{

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "TestCompiler Template";
    }
}

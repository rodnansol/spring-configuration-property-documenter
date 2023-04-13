package org.rodnansol.core.generator.template.compiler;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.data.TemplateData;

public class TestCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "TestCompiler Template";
    }
}

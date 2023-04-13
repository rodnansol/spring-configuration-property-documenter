package org.rodnansol.core.generator.template.compiler;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.data.TemplateData;

public class AnotherTestCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "Another Test Compiler Template";
    }
}

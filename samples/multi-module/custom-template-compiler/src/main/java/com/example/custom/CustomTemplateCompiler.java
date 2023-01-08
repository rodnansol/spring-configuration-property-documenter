package com.example.custom;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.template.TemplateData;

public class CustomTemplateCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "Hello World from custom template compiler";
    }
}

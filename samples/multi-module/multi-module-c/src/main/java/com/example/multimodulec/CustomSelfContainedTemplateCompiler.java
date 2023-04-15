package com.example.multimodulec;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.data.TemplateData;

public class CustomSelfContainedTemplateCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "Custom self contained compiler";
    }
}

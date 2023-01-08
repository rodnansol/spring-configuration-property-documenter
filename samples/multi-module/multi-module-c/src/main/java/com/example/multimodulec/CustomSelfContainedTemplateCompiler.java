package com.example.multimodulec;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.template.TemplateData;

public class CustomSelfContainedTemplateCompiler implements TemplateCompiler {

    @Override
    public String compileTemplate(String templatePath, TemplateData templateData) throws DocumentGenerationException {
        return "Custom self contained compiler";
    }
}

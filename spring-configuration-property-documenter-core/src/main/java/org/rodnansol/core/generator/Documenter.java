package org.rodnansol.core.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Documenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Documenter.class);

    private final MetadataReader metadataReader;
    private final TemplateCompiler templateCompiler;

    public Documenter(MetadataReader metadataReader, TemplateCompiler templateCompiler) {
        this.metadataReader = metadataReader;
        this.templateCompiler = templateCompiler;
    }

    public void readMetadataAndGenerateRenderedFile(CreateDocumentCommand createDocumentCommand) throws IOException {
        LOGGER.debug("Creating document with command:[{}]", createDocumentCommand);
        List<PropertyGroup> propertyGroups = metadataReader.readProperties2(createDocumentCommand.getMetadataFile());
        TemplateData templateData = new TemplateData(createDocumentCommand.getName(), propertyGroups);
        templateData.setGenerationDate(LocalDateTime.now());
        templateData.setDescription(createDocumentCommand.getDescription());
        String content = templateCompiler.compileTemplate(createDocumentCommand.getTemplate(), templateData);
        try (FileWriter fileWriter = new FileWriter(createDocumentCommand.getOutput())) {
            LOGGER.debug("Writing generated content to file:[{}]", createDocumentCommand.getOutput());
            fileWriter.write(content);
        }
    }
}

package org.rodnansol.core.generator.writer;

import org.rodnansol.core.generator.template.MainTemplateData;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.util.CoreFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Class using the {@link MetadataReader} and {@link TemplateCompiler} to read the incoming metadata file and render it to the final file.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Documenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Documenter.class);
    private final MetadataReader metadataReader;
    private final TemplateCompiler templateCompiler;
    private final MetadataInputResolverContext metadataInputResolverContext;

    public Documenter(MetadataReader metadataReader, TemplateCompiler templateCompiler, MetadataInputResolverContext metadataInputResolverContext) {
        this.metadataReader = metadataReader;
        this.templateCompiler = templateCompiler;
        this.metadataInputResolverContext = metadataInputResolverContext;
    }

    /**
     * @param createDocumentCommand
     * @throws IOException
     */
    public void readMetadataAndGenerateRenderedFile(CreateDocumentCommand createDocumentCommand) throws IOException {
        LOGGER.debug("Creating document with command:[{}]", createDocumentCommand);
        MainTemplateData mainTemplateData = createTemplateData(createDocumentCommand);
        String content = templateCompiler.compileTemplate(createDocumentCommand.getTemplate(), mainTemplateData);
        try (FileWriter fileWriter = new FileWriter(CoreFileUtils.initializeFileWithPath(createDocumentCommand.getOutput()))) {
            LOGGER.debug("Writing generated content to file:[{}]", createDocumentCommand.getOutput());
            fileWriter.write(content);
        }
    }

    private MainTemplateData createTemplateData(CreateDocumentCommand createDocumentCommand) throws IOException {
        try (InputStream inputStream = metadataInputResolverContext.getInputStreamFromFile(createDocumentCommand.getProject(), createDocumentCommand.getMetadataInput())) {
            List<PropertyGroup> propertyGroups = metadataReader.readPropertiesAsList(inputStream);
            MainTemplateData mainTemplateData = MainTemplateData.ofMainSection(createDocumentCommand.getName(), propertyGroups);
            mainTemplateData.setGenerationDate(LocalDateTime.now());
            mainTemplateData.setMainDescription(createDocumentCommand.getDescription());
            return mainTemplateData;
        }
    }

}

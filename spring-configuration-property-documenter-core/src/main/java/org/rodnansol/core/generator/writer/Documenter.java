package org.rodnansol.core.generator.writer;

import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.MainTemplateData;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.template.TemplateCompilerMemoryStoreConstants;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.generator.writer.postprocess.PostProcessPropertyGroupsCommand;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;
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
    private final PropertyGroupFilterService propertyGroupFilterService;

    public Documenter(MetadataReader metadataReader, TemplateCompiler templateCompiler, MetadataInputResolverContext metadataInputResolverContext, PropertyGroupFilterService propertyGroupFilterService) {
        this.metadataReader = metadataReader;
        this.templateCompiler = templateCompiler;
        this.metadataInputResolverContext = metadataInputResolverContext;
        this.propertyGroupFilterService = propertyGroupFilterService;
    }

    /**
     * Creates and writes a single document to disk based on the incoming command object.
     *
     * @param createDocumentCommand command object that contains the information for the generation process.
     * @throws IOException if any of the I/O operation fails.
     */
    public void readMetadataAndGenerateRenderedFile(CreateDocumentCommand createDocumentCommand) throws IOException {
        try {
            LOGGER.debug("Creating document with command:[{}]", createDocumentCommand);
            MainTemplateData mainTemplateData = createTemplateData(createDocumentCommand);
            setupMemoryStore(mainTemplateData);
            String content = templateCompiler.compileTemplate(createDocumentCommand.getTemplate(), mainTemplateData);
            try (FileWriter fileWriter = new FileWriter(CoreFileUtils.initializeFileWithPath(createDocumentCommand.getOutput()))) {
                LOGGER.debug("Writing generated content to file:[{}]", createDocumentCommand.getOutput());
                fileWriter.write(content);
            }
        } finally {
            templateCompiler.getMemoryStore().resetMemory();
        }
    }

    private void setupMemoryStore(MainTemplateData mainTemplateData) {
        templateCompiler.getMemoryStore().addItemToMemory(TemplateCompilerMemoryStoreConstants.TEMPLATE_CUSTOMIZATION, mainTemplateData.getTemplateCustomization());
    }

    private MainTemplateData createTemplateData(CreateDocumentCommand createDocumentCommand) throws IOException {
        try (InputStream inputStream = metadataInputResolverContext.getInputStreamFromFile(createDocumentCommand.getProject(), createDocumentCommand.getMetadataInput())) {
            List<PropertyGroup> propertyGroups = metadataReader.readPropertiesAsPropertyGroupList(inputStream);
            TemplateCustomization templateCustomization = createDocumentCommand.getTemplateCustomization();
            filterGroupsAndProperties(createDocumentCommand, propertyGroups, templateCustomization);
            MainTemplateData mainTemplateData = MainTemplateData.ofMainSection(createDocumentCommand.getName(), propertyGroups);
            mainTemplateData.setGenerationDate(LocalDateTime.now());
            mainTemplateData.setMainDescription(createDocumentCommand.getDescription());
            mainTemplateData.setTemplateCustomization(templateCustomization);
            return mainTemplateData;
        }
    }

    private void filterGroupsAndProperties(CreateDocumentCommand createDocumentCommand, List<PropertyGroup> propertyGroups, TemplateCustomization templateCustomization) {
        propertyGroupFilterService.postProcessPropertyGroups(new PostProcessPropertyGroupsCommand(templateCustomization, propertyGroups, createDocumentCommand.getExcludedGroups(), createDocumentCommand.getIncludedGroups(), createDocumentCommand.getExcludedProperties(), createDocumentCommand.getIncludedProperties()));
    }

}

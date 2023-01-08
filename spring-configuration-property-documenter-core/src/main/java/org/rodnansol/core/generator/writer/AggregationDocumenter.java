package org.rodnansol.core.generator.writer;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.MainTemplateData;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.SubTemplateData;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.util.CoreFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class aggregates the incoming inputs into one big output file.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class AggregationDocumenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationDocumenter.class);
    private final MetadataReader metadataReader;
    private final TemplateCompiler templateCompiler;
    private final MetadataInputResolverContext metadataInputResolverContext;

    public AggregationDocumenter(MetadataReader metadataReader, TemplateCompiler templateCompiler, MetadataInputResolverContext metadataInputResolverContext) {
        this.metadataReader = metadataReader;
        this.templateCompiler = templateCompiler;
        this.metadataInputResolverContext = metadataInputResolverContext;
    }

    /**
     * Aggregates the incoming inputs into one big file and writes it to the disk.
     *
     * @param createAggregationCommand command contains necessary information for the aggregation.
     * @throws DocumentGenerationException if the output can not be written to disk or during the template compilation any error occurs.
     */
    public void createDocumentsAndAggregate(CreateAggregationCommand createAggregationCommand) {
        Objects.requireNonNull(createAggregationCommand, "createAggregationCommand is NULL");
        LOGGER.info("Creating documents and aggregating them based on the incoming command:[{}]", createAggregationCommand);
        List<SubTemplateData> subTemplateDataList = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        List<PropertyGroup> propertyGroups = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        for (CombinedInput entry : createAggregationCommand.getCombinedInputs()) {
            LOGGER.info("Processing entry:[{}]", entry);
            try (InputStream inputStream = metadataInputResolverContext.getInputStreamFromFile(createAggregationCommand.getProject(), entry.getInput())) {
                List<PropertyGroup> groups = metadataReader.readPropertiesAsPropertyGroupList(inputStream);
                propertyGroups.addAll(groups);
                subTemplateDataList.add(createModuleTemplateData(entry.getSectionName(), groups, entry.getDescription()));
            } catch (Exception e) {
                LOGGER.warn("Error during reading an entry:[" + entry.getInput() + "]", e);
            }
        }
        createAndWriteContent(createAggregationCommand, subTemplateDataList, propertyGroups);
    }

    private void createAndWriteContent(CreateAggregationCommand createAggregationCommand, List<SubTemplateData> subTemplateDataList, List<PropertyGroup> propertyGroups) {
        TemplateType templateType = createAggregationCommand.getTemplateType();
        MainTemplateData mainTemplateData = createMainTemplateData(createAggregationCommand, propertyGroups);
        mainTemplateData.setSubTemplateDataList(subTemplateDataList);
        String header = templateCompiler.compileTemplate(templateType.getHeaderTemplate(), mainTemplateData);
        String footer = templateCompiler.compileTemplate(templateType.getFooterTemplate(), mainTemplateData);
        String aggregatedContent = subTemplateDataList
            .stream()
            .map(templateData -> templateCompiler.compileTemplate(templateType.getContentTemplate(), templateData))
            .reduce("", String::concat);
        try {
            try (FileWriter fileWriter = new FileWriter(CoreFileUtils.initializeFileWithPath(createAggregationCommand.getOutput()))) {
                LOGGER.debug("Writing aggregated content to file:[{}]", createAggregationCommand.getOutput());
                fileWriter.append(header)
                    .append(aggregatedContent)
                    .append(footer);
            }
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during writing content to file...", e);
        }
    }

    private SubTemplateData createModuleTemplateData(String sectionName, List<PropertyGroup> propertyGroups, String description) {
        SubTemplateData subTemplateData = new SubTemplateData(sectionName, propertyGroups);
        subTemplateData.setGenerationDate(LocalDateTime.now());
        subTemplateData.setModuleDescription(description);
        return subTemplateData;
    }

    private MainTemplateData createMainTemplateData(CreateAggregationCommand createAggregationCommand, List<PropertyGroup> propertyGroups) {
        MainTemplateData mainTemplateData = new MainTemplateData(createAggregationCommand.getAggregatedDocumentHeader(), propertyGroups);
        mainTemplateData.setMainDescription(createAggregationCommand.getDescription());
        mainTemplateData.setGenerationDate(LocalDateTime.now());
        mainTemplateData.setTemplateCustomization(createAggregationCommand.getTemplateCustomization());
        return mainTemplateData;
    }

}

package org.rodnansol.core.generator.writer;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.github.jknack.handlebars.internal.lang3.tuple.ImmutablePair;
import com.github.jknack.handlebars.internal.lang3.tuple.Pair;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.MainTemplateData;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.SubTemplateData;
import org.rodnansol.core.generator.template.TemplateCompiler;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
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
import java.util.Optional;

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
        Pair<List<SubTemplateData>, List<PropertyGroup>> result = createSubTemplateDataAndPropertyGroupList(createAggregationCommand);
        createAndWriteContent(createAggregationCommand, result.getLeft(), result.getRight());
    }

    private Pair<List<SubTemplateData>, List<PropertyGroup>> createSubTemplateDataAndPropertyGroupList(CreateAggregationCommand createAggregationCommand) {
        List<SubTemplateData> subTemplateDataList = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        List<PropertyGroup> propertyGroups = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        for (CombinedInput entry : createAggregationCommand.getCombinedInputs()) {
            LOGGER.info("Processing entry:[{}]", entry);
            try (InputStream inputStream = metadataInputResolverContext.getInputStreamFromFile(createAggregationCommand.getProject(), entry.getInput())) {
                List<PropertyGroup> groups = metadataReader.readPropertiesAsPropertyGroupList(inputStream);
                propertyGroups.addAll(groups);
                subTemplateDataList.add(createModuleTemplateData(createAggregationCommand.getTemplateCustomization(), entry.getSectionName(), groups, entry.getDescription()));
            } catch (Exception e) {
                LOGGER.warn("Error during reading an entry:[" + entry.getInput() + "]", e);
            }
        }
        return new ImmutablePair<>(subTemplateDataList, propertyGroups);
    }

    private void createAndWriteContent(CreateAggregationCommand createAggregationCommand, List<SubTemplateData> subTemplateDataList, List<PropertyGroup> propertyGroups) {
        MainTemplateData mainTemplateData = createMainTemplateData(createAggregationCommand, propertyGroups);
        mainTemplateData.setSubTemplateDataList(subTemplateDataList);

        Optional<CustomTemplate> optionalCustomTemplate = Optional.ofNullable(createAggregationCommand.getCustomTemplate());
        TemplateType templateType = createAggregationCommand.getTemplateType();
        ImmutablePair<String, String> renderedHeaderAndFooter = renderHeaderAndFooter(optionalCustomTemplate, templateType, mainTemplateData);
        String aggregatedContent = renderContent(subTemplateDataList, optionalCustomTemplate, templateType);
        writeRenderedSectionsToFile(createAggregationCommand, renderedHeaderAndFooter, aggregatedContent);
    }

    private ImmutablePair<String, String> renderHeaderAndFooter(Optional<CustomTemplate> optionalCustomTemplate, TemplateType templateType, MainTemplateData mainTemplateData) {
        String headerTemplate = optionalCustomTemplate.map(CustomTemplate::getCustomHeaderTemplate).filter(StringUtils::isNotBlank).orElse(templateType.getHeaderTemplate());
        String footerTemplate = optionalCustomTemplate.map(CustomTemplate::getCustomFooterTemplate).filter(StringUtils::isNotBlank).orElse(templateType.getFooterTemplate());
        String header = templateCompiler.compileTemplate(headerTemplate, mainTemplateData);
        String footer = templateCompiler.compileTemplate(footerTemplate, mainTemplateData);
        return new ImmutablePair<>(header, footer);
    }

    private String renderContent(List<SubTemplateData> subTemplateDataList, Optional<CustomTemplate> optionalCustomTemplate, TemplateType templateType) {
        String contentTemplate = resolveContentTemplate(templateType, optionalCustomTemplate);
        return subTemplateDataList
            .stream()
            .map(templateData -> templateCompiler.compileTemplate(contentTemplate, templateData))
            .reduce("", String::concat);
    }

    private String resolveContentTemplate(TemplateType templateType, Optional<CustomTemplate> optionalCustomTemplate) {
        return optionalCustomTemplate.map(CustomTemplate::getCustomContentTemplate).filter(StringUtils::isNotBlank).orElse(templateType.getContentTemplate());
    }

    private void writeRenderedSectionsToFile(CreateAggregationCommand createAggregationCommand, ImmutablePair<String, String> renderedHeaderAndFooter, String aggregatedContent) {
        try {
            try (FileWriter fileWriter = new FileWriter(CoreFileUtils.initializeFileWithPath(createAggregationCommand.getOutput()))) {
                LOGGER.debug("Writing aggregated content to file:[{}]", createAggregationCommand.getOutput());
                fileWriter.append(renderedHeaderAndFooter.getLeft())
                    .append(aggregatedContent)
                    .append(renderedHeaderAndFooter.getRight());
            }
        } catch (IOException e) {
            throw new DocumentGenerationException("Error during writing content to file...", e);
        }
    }

    private SubTemplateData createModuleTemplateData(TemplateCustomization templateCustomization, String sectionName, List<PropertyGroup> propertyGroups, String description) {
        SubTemplateData subTemplateData = new SubTemplateData(sectionName, propertyGroups);
        subTemplateData.setTemplateCustomization(templateCustomization);
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

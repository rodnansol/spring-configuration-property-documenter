package org.rodnansol.core.generator.writer;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.github.jknack.handlebars.internal.lang3.tuple.ImmutablePair;
import com.github.jknack.handlebars.internal.lang3.tuple.Pair;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.InputFileResolutionStrategy;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.compiler.TemplateCompiler;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerMemoryStoreConstants;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.generator.template.data.MainTemplateData;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.data.SubTemplateData;
import org.rodnansol.core.generator.writer.postprocess.PostProcessPropertyGroupsCommand;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;
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
    private final PropertyGroupFilterService propertyGroupFilterService;

    public AggregationDocumenter(MetadataReader metadataReader, TemplateCompiler templateCompiler, MetadataInputResolverContext metadataInputResolverContext, PropertyGroupFilterService propertyGroupFilterService) {
        this.metadataReader = metadataReader;
        this.templateCompiler = templateCompiler;
        this.metadataInputResolverContext = metadataInputResolverContext;
        this.propertyGroupFilterService = propertyGroupFilterService;
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
        try {
            templateCompiler.getMemoryStore().addItemToMemory(TemplateCompilerMemoryStoreConstants.TEMPLATE_CUSTOMIZATION, createAggregationCommand.getTemplateCustomization());
            createAndWriteContent(createAggregationCommand, result.getLeft(), result.getRight());
        } finally {
            templateCompiler.getMemoryStore().resetMemory();
        }
    }

    private Pair<List<SubTemplateData>, List<PropertyGroup>> createSubTemplateDataAndPropertyGroupList(CreateAggregationCommand createAggregationCommand) {
        List<SubTemplateData> subTemplateDataList = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        List<PropertyGroup> propertyGroups = new ArrayList<>(createAggregationCommand.getCombinedInputs().size());
        for (CombinedInput entry : createAggregationCommand.getCombinedInputs()) {
            LOGGER.info("Processing entry:[{}]", entry);
            try (InputStream inputStream = getInputStreamFromFile(createAggregationCommand, entry)) {
                List<PropertyGroup> groups = metadataReader.readPropertiesAsPropertyGroupList(inputStream);
                filterGroupsAndProperties(createAggregationCommand.getTemplateCustomization(), entry, groups);
                propertyGroups.addAll(groups);
                subTemplateDataList.add(createModuleTemplateData(createAggregationCommand.getTemplateCustomization(), entry.getSectionName(), groups, entry.getDescription()));
            } catch (DocumentGenerationException e) {
                // [#68] Generate an empty file if the spring-configuration-metadata.json is missing
                // Because of the introduction of the "failOnMissingInput" attribute this exception must be propagated
                // Other exceptions can be just logged out
                throw e;
            } catch (Exception e) {
                LOGGER.warn("Error during reading an entry:[" + entry.getInput() + "]", e);
            }
        }
        return new ImmutablePair<>(subTemplateDataList, propertyGroups);
    }

    private InputStream getInputStreamFromFile(CreateAggregationCommand createAggregationCommand, CombinedInput entry) {
        return metadataInputResolverContext.getInputStreamFromFile(createAggregationCommand.getProject(),
            entry.getInput(),
            InputFileResolutionStrategy.ofBooleanValue(createAggregationCommand.isFailOnMissingInput()));
    }

    void filterGroupsAndProperties(TemplateCustomization templateCustomization, CombinedInput entry, List<PropertyGroup> groups) {
        propertyGroupFilterService.postProcessPropertyGroups(new PostProcessPropertyGroupsCommand(templateCustomization, groups, entry.getExcludedGroups(), entry.getIncludedGroups(), entry.getExcludedProperties(), entry.getIncludedProperties()));
    }

    private void createAndWriteContent(CreateAggregationCommand createAggregationCommand, List<SubTemplateData> subTemplateDataList, List<PropertyGroup> propertyGroups) {
        MainTemplateData mainTemplateData = createMainTemplateData(createAggregationCommand, propertyGroups);
        mainTemplateData.setSubTemplateDataList(subTemplateDataList);
        ResolvedTemplate resolvedTemplate = new ResolvedTemplate(createAggregationCommand);
        ImmutablePair<String, String> renderedHeaderAndFooter = renderHeaderAndFooter(resolvedTemplate, mainTemplateData);
        String aggregatedContent = renderContent(resolvedTemplate, subTemplateDataList);
        writeRenderedSectionsToFile(createAggregationCommand, renderedHeaderAndFooter, aggregatedContent);
    }

    private ImmutablePair<String, String> renderHeaderAndFooter(ResolvedTemplate resolvedTemplate, MainTemplateData mainTemplateData) {
        String header = templateCompiler.compileTemplate(resolvedTemplate.getHeaderTemplate(), mainTemplateData);
        String footer = templateCompiler.compileTemplate(resolvedTemplate.getFooterTemplate(), mainTemplateData);
        return new ImmutablePair<>(header, footer);
    }

    private String renderContent(ResolvedTemplate resolvedTemplate, List<SubTemplateData> subTemplateDataList) {
        return subTemplateDataList
            .stream()
            .map(templateData -> templateCompiler.compileTemplate(resolvedTemplate.getContentTemplate(), templateData))
            .reduce("", String::concat);
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

    private SubTemplateData createModuleTemplateData(TemplateCustomization templateCustomization, String sectionName, List<PropertyGroup> propertyGroups, String moduleDescription) {
        SubTemplateData subTemplateData = new SubTemplateData(sectionName, propertyGroups);
        subTemplateData.setTemplateCustomization(templateCustomization);
        subTemplateData.setGenerationDate(LocalDateTime.now());
        subTemplateData.setModuleDescription(moduleDescription);
        return subTemplateData;
    }

    private MainTemplateData createMainTemplateData(CreateAggregationCommand createAggregationCommand, List<PropertyGroup> propertyGroups) {
        MainTemplateData mainTemplateData = new MainTemplateData(createAggregationCommand.getAggregatedDocumentHeader(), propertyGroups);
        mainTemplateData.setMainDescription(createAggregationCommand.getDescription());
        mainTemplateData.setGenerationDate(LocalDateTime.now());
        mainTemplateData.setTemplateCustomization(createAggregationCommand.getTemplateCustomization());
        return mainTemplateData;
    }

    static class ResolvedTemplate {
        private final String headerTemplate;
        private final String contentTemplate;
        private final String footerTemplate;

        public ResolvedTemplate(CreateAggregationCommand createAggregationCommand) {
            Optional<CustomTemplate> optionalCustomTemplate = Optional.ofNullable(createAggregationCommand.getCustomTemplate());
            TemplateCustomization templateCustomization = createAggregationCommand.getTemplateCustomization();
            TemplateType templateType = createAggregationCommand.getTemplateType();
            this.headerTemplate = optionalCustomTemplate.map(CustomTemplate::getCustomHeaderTemplate)
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> templateType.getHeaderTemplate(templateCustomization.getTemplateMode()));
            this.footerTemplate = optionalCustomTemplate.map(CustomTemplate::getCustomFooterTemplate)
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> templateType.getFooterTemplate(templateCustomization.getTemplateMode()));
            this.contentTemplate = optionalCustomTemplate
                .map(CustomTemplate::getCustomContentTemplate)
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> templateType.getContentTemplate(createAggregationCommand.getTemplateCustomization().getTemplateMode()));
        }

        public String getHeaderTemplate() {
            return headerTemplate;
        }

        public String getContentTemplate() {
            return contentTemplate;
        }

        public String getFooterTemplate() {
            return footerTemplate;
        }
    }

}

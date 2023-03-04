package org.rodnansol.core.action;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.generator.writer.CreateDocumentCommand;
import org.rodnansol.core.generator.writer.Documenter;
import org.rodnansol.core.generator.writer.PropertyGroupFilterService;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.util.CoreFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Action class for single document generation.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class DocumentGenerationAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentGenerationAction.class);
    private final Project project;
    private final String name;
    private final TemplateType templateType;
    private final File metadataInput;
    private final TemplateCustomization templateCustomization;
    private String description;
    private String template;
    private File outputFile;
    private String templateCompilerName = HandlebarsTemplateCompiler.class.getName();
    private List<String> excludedGroups;
    private List<String> includedGroups;
    private List<String> excludedProperties;
    private List<String> includedProperties;

    public DocumentGenerationAction(Project project, String name, TemplateCustomization templateCustomization, TemplateType templateType, File metadataInput) {
        this.project = Objects.requireNonNull(project, "project is NULL");
        this.name = Objects.requireNonNull(name, "name is NULL");
        this.templateCustomization = Objects.requireNonNull(templateCustomization);
        this.templateType = Objects.requireNonNull(templateType);
        this.metadataInput = Objects.requireNonNull(metadataInput);
    }

    /**
     * Sets up the template files and the output file, then executes the generation.
     */
    public void execute() {
        initializeTemplate(templateType);
        checkOutputFileAndCreateIfDoesNotExist(templateType);
        LOGGER.info("Creating property document file at:[{}]", outputFile);
        generateDocument();
    }

    private void generateDocument() throws DocumentGenerationException {
        try {
            Documenter documenter = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);
            CreateDocumentCommand command = new CreateDocumentCommand(project, name, metadataInput, template, outputFile, templateCustomization);
            command.setDescription(description);
            command.setExcludedGroups(excludedGroups);
            command.setIncludedGroups(includedGroups);
            command.setExcludedProperties(excludedProperties);
            command.setIncludedProperties(includedProperties);
            documenter.readMetadataAndGenerateRenderedFile(command);
        } catch (IOException e) {
            throw new DocumentGenerationException("Unable to create final document", e);
        }
    }

    private void initializeTemplate(TemplateType templateType) {
        if (template == null) {
            template = templateType.getSingleTemplate();
        }
    }

    private void checkOutputFileAndCreateIfDoesNotExist(TemplateType templateType) throws DocumentGenerationException {
        if (outputFile != null) {
            String extension = templateType.getExtension();
            if (!outputFile.getName().endsWith(extension)) {
                throw new DocumentGenerationException("Output file does not have the proper extension, requested file name:[" + outputFile + "] with requested extension:[" + extension + "]");
            }
        } else {
            outputFile = CoreFileUtils.initializeFileWithPath(project.getDefaultTargetFilePath(templateType));
        }
    }

    public void setTemplateCompilerName(String templateCompilerName) {
        this.templateCompilerName = templateCompilerName;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExcludedGroups(List<String> excludedGroups) {
        this.excludedGroups = excludedGroups;
    }

    public void setIncludedGroups(List<String> includedGroups) {
        this.includedGroups = includedGroups;
    }

    public void setExcludedProperties(List<String> excludedProperties) {
        this.excludedProperties = excludedProperties;
    }

    public void setIncludedProperties(List<String> includedProperties) {
        this.includedProperties = includedProperties;
    }

}

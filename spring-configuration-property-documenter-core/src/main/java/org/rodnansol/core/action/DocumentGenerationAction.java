package org.rodnansol.core.action;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.writer.CreateDocumentCommand;
import org.rodnansol.core.generator.writer.Documenter;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.util.CoreFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

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
    private final String description;
    private final String type;
    private final File metadataInput;
    private String template;
    private File outputFile;
    private String templateCompilerName = HandlebarsTemplateCompiler.class.getName();

    public DocumentGenerationAction(Project project, String name, String description, String template, String type, File metadataInput, File outputFile) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.template = template;
        this.type = type;
        this.metadataInput = metadataInput;
        this.outputFile = outputFile;
    }

    public void execute() {
        TemplateType templateType = TemplateType.valueOf(type);
        initializeTemplate(templateType);
        checkOutputFileAndCreateIfDoesNotExist(templateType);
        LOGGER.info("Creating property document file at:[{}]", outputFile);
        generateDocument();
    }

    private void generateDocument() throws DocumentGenerationException {
        try {
            Documenter documenter = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE);
            CreateDocumentCommand command = new CreateDocumentCommand(project, name, metadataInput, template, outputFile);
            command.setDescription(description);
            documenter.readMetadataAndGenerateRenderedFile(command);
        } catch (IOException e) {
            throw new DocumentGenerationException("Unable to create final document", e);
        }
    }

    private void initializeTemplate(TemplateType templateType) {
        if (template == null) {
            template = templateType.findSingleTemplate();
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


    public String getTemplateCompilerName() {
        return templateCompilerName;
    }

    public void setTemplateCompilerName(String templateCompilerName) {
        this.templateCompilerName = templateCompilerName;
    }

}

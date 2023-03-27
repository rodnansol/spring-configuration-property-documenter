package org.rodnansol.gradle.tasks;

import groovy.lang.Closure;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.generator.template.customization.HtmlTemplateCustomization;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.generator.template.customization.XmlTemplateCustomization;
import org.rodnansol.core.generator.writer.AggregationDocumenter;
import org.rodnansol.core.generator.writer.CombinedInput;
import org.rodnansol.core.generator.writer.CreateAggregationCommand;
import org.rodnansol.core.generator.writer.CustomTemplate;
import org.rodnansol.core.generator.writer.postprocess.PropertyGroupFilterService;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.gradle.GradleProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This goal reads all the given `spring-configuration-metadata.json` files from the given/specified sources, and it will be generating one single document that will contain all documentation created by the given sources.
 * <p>
 * This goal is good for a multimodule setup, it is able to read multiple files and aggregate them.
 *
 * @author nandorholozsnyak
 * @author tkhadiradeo
 * @since 0.5.0
 */
public abstract class GenerateAndAggregateDocumentsTask extends ConventionTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateAndAggregateDocumentsTask.class);

    /**
     * List of the metadata inputs.
     *
     * @since 0.5.0
     */
    @Input
    List<AggregationInput> metadataInputs = new ArrayList<>();

    /**
     * Main header section name.
     *
     * @since 0.5.0
     */
    @Input
    @Option(option = "documentName", description = "Name/header of the generated document")
    private String documentName;

    /**
     * Main module description.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "documentDescription", description = "Description")
    private String documentDescription;

    /**
     * Type of the document.
     * <p>
     * The following template types are supported:
     * <ul>
     *     <li>MARKDOWN</li>
     *     <li>ADOC</li>
     *     <li>HTML</li>
     *     <li>XML</li>
     * </ul>
     *
     * @since 0.5.0
     */
    @Input
    @Option(option = "type", description = "Type of the final rendered document")
    private TemplateType type;

    /**
     * HTML template customization object to configure the template.
     *
     * @since 0.5.0
     */
    @Internal
    private HtmlTemplateCustomization htmlCustomization = new HtmlTemplateCustomization();

    /**
     * Markdown template customization object to configure the template.
     *
     * @since 0.5.0
     */
    @Internal
    private MarkdownTemplateCustomization markdownCustomization = new MarkdownTemplateCustomization();

    /**
     * AsciiDoc template customization object to configure the template.
     *
     * @since 0.5.0
     */
    @Internal
    private AsciiDocTemplateCustomization asciiDocCustomization = new AsciiDocTemplateCustomization();

    /**
     * XML template customization object to configure the template.
     *
     * @since 0.5.0
     */
    @Internal
    private XmlTemplateCustomization xmlCustomization = new XmlTemplateCustomization();

    /**
     * Output file.
     *
     * @since 0.5.0
     */
    @OutputFile
    @Option(option = "outputFile", description = "Output file")
    private File outputFile;

    /**
     * Template compiler class's fully qualified name .
     * <p>
     * With this option you can use your own template compiler implementation if the default {@link HandlebarsTemplateCompiler}. based one is not enough.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "templateCompilerName", description = "Name of the template compiler, by default Handlebars will be used")
    private String templateCompilerName = HandlebarsTemplateCompiler.class.getName();

    /**
     * Custom header template file.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "headerTemplate", description = "Template file to be used in the header section generation")
    private String headerTemplate;

    /**
     * Custom content template file.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "contentTemplate", description = "Template file to be used in the content section generation")
    private String contentTemplate;

    /**
     * Custom footer template file.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "footerTemplate", description = "Template file to be used in the footer section generation")
    private String footerTemplate;

    @TaskAction
    public void execute() {
        LOGGER.info("Creating aggregation based on the following inputs:[{}]", metadataInputs);
        AggregationDocumenter aggregationDocumenter = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);
        CreateAggregationCommand createAggregationCommand = createAggregationCommand();
        aggregationDocumenter.createDocumentsAndAggregate(createAggregationCommand);
    }

    private CreateAggregationCommand createAggregationCommand() {
        List<CombinedInput> combinedInputs = metadataInputs == null ? new ArrayList<>() : metadataInputs.stream().map(this::mapToCombinedInput).collect(Collectors.toList());
        GradleProject gradleProject = ProjectFactory.ofGradleProject(getProject().getProjectDir(), documentName, List.of());
        CreateAggregationCommand createAggregationCommand = new CreateAggregationCommand(gradleProject, documentName, combinedInputs, type, getActualTemplateCustomization(), outputFile);
        createAggregationCommand.setDescription(documentDescription);
        createAggregationCommand.setCustomTemplate(new CustomTemplate(headerTemplate, contentTemplate, footerTemplate));
        return createAggregationCommand;
    }

    private CombinedInput mapToCombinedInput(AggregationInput aggregationInput) {
        CombinedInput combinedInput = new CombinedInput(aggregationInput.getInput(), aggregationInput.getName());
        combinedInput.setDescription(aggregationInput.getDescription());
        combinedInput.setExcludedGroups(aggregationInput.getExcludedGroups());
        combinedInput.setIncludedGroups(aggregationInput.getIncludedGroups());
        combinedInput.setIncludedProperties(aggregationInput.getIncludedProperties());
        combinedInput.setExcludedProperties(aggregationInput.getExcludedProperties());
        return combinedInput;
    }

    private TemplateCustomization getActualTemplateCustomization() {
        switch (type) {
            case MARKDOWN:
                return markdownCustomization;
            case ADOC:
                return asciiDocCustomization;
            case HTML:
                return htmlCustomization;
            case XML:
                return xmlCustomization;
        }
        throw new IllegalStateException("There is no template customization set for the current run");
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public HtmlTemplateCustomization getHtmlCustomization() {
        return htmlCustomization;
    }

    public void setHtmlCustomization(HtmlTemplateCustomization htmlCustomization) {
        this.htmlCustomization = htmlCustomization;
    }

    public MarkdownTemplateCustomization getMarkdownCustomization() {
        return markdownCustomization;
    }

    public void setMarkdownCustomization(MarkdownTemplateCustomization markdownCustomization) {
        this.markdownCustomization = markdownCustomization;
    }

    public AsciiDocTemplateCustomization getAsciiDocCustomization() {
        return asciiDocCustomization;
    }

    public void setAsciiDocCustomization(AsciiDocTemplateCustomization asciiDocCustomization) {
        this.asciiDocCustomization = asciiDocCustomization;
    }

    public XmlTemplateCustomization getXmlCustomization() {
        return xmlCustomization;
    }

    public void setXmlCustomization(XmlTemplateCustomization xmlCustomization) {
        this.xmlCustomization = xmlCustomization;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getTemplateCompilerName() {
        return templateCompilerName;
    }

    public void setTemplateCompilerName(String templateCompilerName) {
        this.templateCompilerName = templateCompilerName;
    }

    public String getHeaderTemplate() {
        return headerTemplate;
    }

    public void setHeaderTemplate(String headerTemplate) {
        this.headerTemplate = headerTemplate;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate;
    }

    public String getFooterTemplate() {
        return footerTemplate;
    }

    public void setFooterTemplate(String footerTemplate) {
        this.footerTemplate = footerTemplate;
    }

    public List<AggregationInput> getMetadataInputs() {
        return metadataInputs;
    }

    /**
     * DSL entry point for the {@link GenerateAndAggregateDocumentsTask#markdownCustomization} field.
     */
    public void markdownCustomization(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(markdownCustomization);
        closure.call();
    }

    /**
     * DSL entry point for the {@link GenerateAndAggregateDocumentsTask#asciiDocCustomization} field.
     */
    public void asciiDocCustomization(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(asciiDocCustomization);
        closure.call();
    }

    /**
     * DSL entry point for the {@link GenerateAndAggregateDocumentsTask#htmlCustomization} field.
     */
    public void htmlCustomization(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(htmlCustomization);
        closure.call();
    }

    /**
     * DSL entry point for the {@link GenerateAndAggregateDocumentsTask#xmlCustomization} field.
     */
    public void xmlCustomization(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(xmlCustomization);
        closure.call();
    }

    /**
     * DSL entry point for the {@link GenerateAndAggregateDocumentsTask#metadataInputs} field.
     */
    public void metadataInputs(Closure closure) {
        metadataInputs = new ArrayList<>();
        closure.setDelegate(new AggregationInputBuilder());
        closure.call();
    }

    /**
     * Inner builder class for delegation.
     */
    public class AggregationInputBuilder {

        public void metadata(Closure closure) {
            AggregationInput input = new AggregationInput();
            closure.setDelegate(input);
            closure.call();
            metadataInputs.add(input);
        }
    }

}

package org.rodnansol.gradle;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This goal reads all the given `spring-configuration-metadata.json` files from the given/specified sources and it will be generating one single document that will contain all documentation created by the given sources. This goal is good for a multi module setup, it is able to read multiple files and aggregate them.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class GenerateAndAggregateDocumentsMojo {

    private static final Logger logger = LoggerFactory.getLogger(GenerateAndAggregateDocumentsMojo.class);

    protected static final String GOAL_NAME = "generate-and-aggregate-documents";

    /**
     * Maven project instance.
     */
    private String project;

    private String baseDir;

    private List<String> modules;

    List<AggregationMojoInput> inputs;

    /**
     * Main header section name.
     *
     * @since 0.1.0
     */
    String name;

    /**
     * Main module description.
     *
     * @since 0.1.0
     */
    private String description;

    /**
     * Type of the document.
     * <p>
     * The following template types are supported:
     * <ul>
     *     <li>MARKDOWN</li>
     *     <li>ADOC</li>
     *     <li>HTML</li>
     *     <li>XML (Since 0.2.0)</li>
     * </ul>
     *
     * @since 0.1.0
     */
    private TemplateType type = TemplateType.MARKDOWN;

    /**
     * HTML template customization object to configure the template.
     *
     * @since 0.2.0
     */
    private HtmlTemplateCustomization htmlCustomization = new HtmlTemplateCustomization();

    /**
     * Markdown template customization object to configure the template.
     *
     * @since 0.2.0
     */
    private MarkdownTemplateCustomization markdownCustomization = new MarkdownTemplateCustomization();

    /**
     * AsciiDoc template customization object to configure the template.
     *
     * @since 0.2.0
     */
    private AsciiDocTemplateCustomization asciiDocCustomization = new AsciiDocTemplateCustomization();

    /**
     * XML template customization object to configure the template.
     *
     * @since 0.2.0
     */
    private XmlTemplateCustomization xmlCustomization = new XmlTemplateCustomization();

    /**
     * Output file.
     *
     * @since 0.1.0
     */
    private File outputFile;

    /**
     * Template compiler class's fully qualified name .
     * <p>
     * With this option you can use your own template compiler implementation if the default {@link HandlebarsTemplateCompiler}. based one is not enough.
     *
     * @since 0.2.0
     */
    private String templateCompilerName = HandlebarsTemplateCompiler.class.getName();

    /**
     * Custom header template file.
     *
     * @since 0.2.1
     */
    private String headerTemplate;

    /**
     * Custom content template file.
     *
     * @since 0.2.1
     */
    private String contentTemplate;

    /**
     * Custom footer template file.
     *
     * @since 0.2.1
     */
    private String footerTemplate;

    public void execute() {
        logger.info("start executing at {}", LocalDateTime.now());
        AggregationDocumenter aggregationDocumenter = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);
        CreateAggregationCommand createAggregationCommand = createAggregationCommand();
        aggregationDocumenter.createDocumentsAndAggregate(createAggregationCommand);
        logger.info("end executing at {}", LocalDateTime.now());
    }

    private CreateAggregationCommand createAggregationCommand() {
        List<CombinedInput> combinedInputs = inputs == null ? new ArrayList<>() : inputs.stream().map(this::mapToCombinedInput).collect(Collectors.toList());
        logger.info("configuring the project at {}", LocalDateTime.now());
        org.rodnansol.core.project.gradle.GradleProject gradleProject = ProjectFactory.ofGradleProject(new File(baseDir), name, modules == null ? new ArrayList<>() : modules);
        logger.info("running plugin on {}", gradleProject.getName());
        CreateAggregationCommand createAggregationCommand = new CreateAggregationCommand(gradleProject, name, combinedInputs, type, getActualTemplateCustomization(), outputFile);
        createAggregationCommand.setDescription(description);
        createAggregationCommand.setCustomTemplate(new CustomTemplate(headerTemplate, contentTemplate, footerTemplate));
        return createAggregationCommand;
    }

    private CombinedInput mapToCombinedInput(AggregationMojoInput aggregationMojoInput) {
        CombinedInput combinedInput = new CombinedInput(aggregationMojoInput.getInput(), aggregationMojoInput.getName());
        combinedInput.setDescription(aggregationMojoInput.getDescription());
        combinedInput.setExcludedGroups(aggregationMojoInput.getExcludedGroups());
        combinedInput.setIncludedGroups(aggregationMojoInput.getIncludedGroups());
        combinedInput.setIncludedProperties(aggregationMojoInput.getIncludedProperties());
        combinedInput.setExcludedProperties(aggregationMojoInput.getExcludedProperties());
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

    public void setBaseDir(final String baseDir) {
        this.baseDir = baseDir;
        this.outputFile = new File(baseDir + "-generated/aggregated-md.md");
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    public String getProject() {
        return this.project;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setModules(final List<String> modules) {
        this.modules = modules;
    }

    public List<String> getModules() {
        return this.modules;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}

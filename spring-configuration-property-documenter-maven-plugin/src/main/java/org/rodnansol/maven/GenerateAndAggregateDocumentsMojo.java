package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.handlebars.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerFactory;
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

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This goal reads all the given `spring-configuration-metadata.json` files from the given/specified sources and it will be generating one single document that will contain all documentation created by the given sources. This goal is good for a multi module setup, it is able to read multiple files and aggregate them.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = GenerateAndAggregateDocumentsMojo.GOAL_NAME)
public class GenerateAndAggregateDocumentsMojo extends AbstractMojo {

    protected static final String GOAL_NAME = "generate-and-aggregate-documents";

    /**
     * Maven project instance.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    /**
     * Main header section name.
     *
     * @since 0.1.0
     */
    @Parameter(property = "name", required = true, defaultValue = "${project.name}")
    String name;

    /**
     * Main module description.
     *
     * @since 0.1.0
     */
    @Parameter(property = "description", defaultValue = "${project.description}")
    String description;

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
    @Parameter(property = "type", defaultValue = "MARKDOWN")
    TemplateType type;

    /**
     * HTML template customization object to configure the template.
     *
     * @since 0.2.0
     */
    @Parameter(property = "htmlCustomization")
    HtmlTemplateCustomization htmlCustomization;

    /**
     * Markdown template customization object to configure the template.
     *
     * @since 0.2.0
     */
    @Parameter(property = "markdownCustomization")
    MarkdownTemplateCustomization markdownCustomization;

    /**
     * AsciiDoc template customization object to configure the template.
     *
     * @since 0.2.0
     */
    @Parameter(property = "asciiDocCustomization")
    AsciiDocTemplateCustomization asciiDocCustomization;

    /**
     * XML template customization object to configure the template.
     *
     * @since 0.2.0
     */
    @Parameter(property = "xmlCustomization")
    XmlTemplateCustomization xmlCustomization;

    /**
     * Input files and additional configuration.
     *
     * @since 0.1.0
     */
    @Parameter(property = "inputs", required = true)
    List<AggregationMojoInput> inputs;

    /**
     * Output file.
     *
     * @since 0.1.0
     */
    @Parameter(property = "outputFile", required = true)
    File outputFile;

    /**
     * Template compiler class's fully qualified name .
     * <p>
     * With this option you can use your own template compiler implementation if the default {@link HandlebarsTemplateCompiler}. based one is not enough.
     *
     * @since 0.2.0
     */
    @Parameter(property = "templateCompilerName")
    String templateCompilerName = HandlebarsTemplateCompiler.class.getName();

    /**
     * Custom header template file.
     *
     * @since 0.2.1
     */
    @Parameter(property = "headerTemplate")
    String headerTemplate;

    /**
     * Custom content template file.
     *
     * @since 0.2.1
     */
    @Parameter(property = "contentTemplate")
    String contentTemplate;

    /**
     * Custom footer template file.
     *
     * @since 0.2.1
     */
    @Parameter(property = "footerTemplate")
    String footerTemplate;

    @Override
    public void execute() {
        AggregationDocumenter aggregationDocumenter = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE, PropertyGroupFilterService.INSTANCE);
        CreateAggregationCommand createAggregationCommand = createAggregationCommand();
        aggregationDocumenter.createDocumentsAndAggregate(createAggregationCommand);
    }

    private CreateAggregationCommand createAggregationCommand() {
        List<CombinedInput> combinedInputs = inputs.stream().map(this::mapToCombinedInput).collect(Collectors.toList());
        org.rodnansol.core.project.maven.MavenProject mavenProject = ProjectFactory.ofMavenProject(project.getBasedir(), name, project.getModules());
        CreateAggregationCommand createAggregationCommand = new CreateAggregationCommand(mavenProject, name, combinedInputs, type, getActualTemplateCustomization(), outputFile);
        createAggregationCommand.setDescription(description);
        createAggregationCommand.setCustomTemplate(new CustomTemplate(headerTemplate, contentTemplate, footerTemplate));
        return createAggregationCommand;
    }

    private CombinedInput mapToCombinedInput(AggregationMojoInput aggregationMojoInput) {
        CombinedInput combinedInput = new CombinedInput(aggregationMojoInput.getInput(), aggregationMojoInput.getName(), aggregationMojoInput.getDescription());
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
}

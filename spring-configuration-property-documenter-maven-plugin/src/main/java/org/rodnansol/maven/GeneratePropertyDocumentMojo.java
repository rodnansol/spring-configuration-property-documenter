package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.action.DocumentGenerationAction;
import org.rodnansol.core.generator.template.handlebars.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.*;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This goal reads the `spring-configuration-metadata.json` file from any given source (input file, directory or JAR file) and generates a single document for the given module. It is good to document a single application or a single module.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = GeneratePropertyDocumentMojo.GENERATE_PROPERTY_DOCUMENT)
public class GeneratePropertyDocumentMojo extends AbstractMojo {

    protected static final String GENERATE_PROPERTY_DOCUMENT = "generate-property-document";
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    /**
     * Name that should be generated to the final document.
     *
     * @since 0.1.0
     */
    @Parameter(property = "name", defaultValue = "${project.name}")
    String name;

    /**
     * Description about the project.
     *
     * @since 0.1.0
     */
    @Parameter(property = "description", defaultValue = "${project.description}")
    String description;

    /**
     * The template to be used, if not specified it will be resolved by the {@link GeneratePropertyDocumentMojo#type} field.
     *
     * @since 0.1.0
     */
    @Parameter(property = "template")
    String template;

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
     * Metadata input that can be:
     * <ul>
     *     <li>A path to JSON file for example: <b>target/classes/META-INF/spring-configuration-metadata.json</b></li>
     *     <li>A directory that contains the file</li>
     *     <li>A jar/zip file that contains the file within the following entry <b>META-INF/spring-configuration-metadata.json</b></li>
     * </ul>
     *
     * @since 0.1.0
     */
    @Parameter(property = "metadataInput", defaultValue = "target/classes/META-INF/spring-configuration-metadata.json")
    File metadataInput;

    /**
     * Output file.
     *
     * @since 0.1.0
     */
    @Parameter(property = "outputFile")
    File outputFile;

    /**
     * Define if the process should fail on an error or not.
     *
     * @since 0.1.0
     */
    @Parameter(property = "failOnError", defaultValue = "false")
    boolean failOnError;

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
     * List of excluded properties.
     *
     * @since 0.4.0
     */
    @Parameter(property = "excludedProperties")
    List<String> excludedProperties = new ArrayList<>();

    /**
     * List of included properties.
     *
     * @since 0.4.0
     */
    @Parameter(property = "includedProperties")
    List<String> includedProperties = new ArrayList<>();

    /**
     * List of excluded groups.
     *
     * @since 0.4.0
     */
    @Parameter(property = "excludedGroups")
    List<String> excludedGroups = new ArrayList<>();

    /**
     * List of included groups.
     *
     * @since 0.4.0
     */
    @Parameter(property = "includedGroups")
    List<String> includedGroups = new ArrayList<>();


    @Override
    public void execute() {
        try {
            DocumentGenerationAction documentGenerationAction = setupAction();
            documentGenerationAction.execute();
        } catch (Exception e) {
            if (failOnError) {
                throw new RuntimeException(e);
            } else {
                getLog().warn("Error during file generation, failOnError is set to false, check the logs please....", e);
            }
        }
    }

    private DocumentGenerationAction setupAction() {
        org.rodnansol.core.project.maven.MavenProject mavenProject = ProjectFactory.ofMavenProject(project.getBasedir(), project.getName(), project.getModules());
        DocumentGenerationAction documentGenerationAction = new DocumentGenerationAction(mavenProject, name, getActualTemplateCustomization(), type, metadataInput);
        documentGenerationAction.setTemplateCompilerName(templateCompilerName);
        documentGenerationAction.setDescription(description);
        documentGenerationAction.setTemplate(template);
        documentGenerationAction.setOutputFile(outputFile);
        documentGenerationAction.setExcludedGroups(excludedGroups);
        documentGenerationAction.setIncludedGroups(includedGroups);
        documentGenerationAction.setExcludedProperties(excludedProperties);
        documentGenerationAction.setIncludedProperties(includedProperties);
        return documentGenerationAction;
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

package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.action.DocumentGenerationAction;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;

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
     */
    @Parameter(property = "name", defaultValue = "${project.name}")
    String name;

    /**
     * Description about the project.
     */
    @Parameter(property = "description", defaultValue = "${project.description}")
    String description;

    /**
     * The template to be used, if not specified it will be resolved by the {@link GeneratePropertyDocumentMojo#type} field.
     */
    @Parameter(property = "template")
    String template;

    /**
     * Type of the document.
     * <p>
     * The following template types are supported:
     * <ul>
     *     <li>MARKDOWN</li>
     *     <li>ADOC</li>
     *     <li>HTML</li>
     * </ul>
     */
    @Parameter(property = "type", defaultValue = "MARKDOWN")
    String type;

    /**
     * Metadata input that can be:
     * <ul>
     *     <li>A path to JSON file for example: <b>target/classes/META-INF/spring-configuration-metadata.json</b></li>
     *     <li>A directory that contains the file</li>
     *     <li>A jar/zip file that contains the file within the following entry <b>META-INF/spring-configuration-metadata.json</b></li>
     * </ul>
     */
    @Parameter(property = "metadataInput", defaultValue = "target/classes/META-INF/spring-configuration-metadata.json")
    File metadataInput;

    /**
     * Output file.
     */
    @Parameter(property = "outputFile")
    File outputFile;

    /**
     * Define if the process should fail on an error or not.
     */
    @Parameter(property = "failOnError", defaultValue = "false")
    boolean failOnError;

    @Override
    public void execute() {
        try {
            new DocumentGenerationAction(
                ProjectFactory.ofMavenProject(project.getBasedir(), project.getName(), project.getModules()), name, description, template, type, metadataInput, outputFile)
                .execute();
        } catch (Exception e) {
            if (failOnError) {
                throw new RuntimeException(e);
            } else {
                getLog().warn("Error during file generation, failOnError is set to false, check the logs please....", e);
            }
        }
    }

}

package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.util.Project;
import org.rodnansol.core.action.DocumentGenerationAction;

import java.io.File;

/**
 * Maven goal that invokes the {@link DocumentGenerationAction} with the incoming parameters.
 * <p>
 * Users are able to customize the execution of the goal.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = "generate-property-document")
public class GeneratePropertyDocumentMojo extends AbstractMojo {

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
     * The metadata file that must be read and used for the document generation.
     */
    @Parameter(property = "metadataFile", defaultValue = "target/classes/META-INF/spring-configuration-metadata.json")
    File metadataFile;

    /**
     * Output file.
     */
    @Parameter(property = "outputFile")
    File outputFile;

    @Override
    public void execute() throws MojoExecutionException {
        new DocumentGenerationAction(Project.ofMavenProject(project.getBasedir(), project.getName(), project.getModules()), name, description, template, type, metadataFile, outputFile)
            .execute();
    }

}

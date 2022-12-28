package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.action.AggregationAction;
import org.rodnansol.core.util.Project;

import java.io.File;
import java.util.List;

/**
 * Aggregates the documents in the child modules.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = "aggregate-documents")
public class AggregateDocumentsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "inputFiles")
    List<File> inputFiles;

    @Parameter(property = "outputFile")
    File outputFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new AggregationAction(Project.ofMavenProject(project.getBasedir(), project.getName(), project.getModules()), inputFiles, outputFile)
            .executeAggregation();
    }
}

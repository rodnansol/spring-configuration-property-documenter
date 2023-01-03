package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.action.AggregationAction;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;
import java.util.List;

/**
 * This goal reads all the given files, and it will aggregate them into one big file. It is good if you have multiple different template based documentation, and you would like to aggregate them into one. *This is not going to generate documents only aggregate the given ones, this has a different purpose than the `generate-and-aggregate-documents`
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = AggregateDocumentsMojo.AGGREGATE_DOCUMENTS)
public class AggregateDocumentsMojo extends AbstractMojo {

    protected static final String AGGREGATE_DOCUMENTS = "aggregate-documents";
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "inputFiles")
    List<File> inputFiles;

    @Parameter(property = "outputFile")
    File outputFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new AggregationAction(ProjectFactory.ofMavenProject(project.getBasedir(), project.getName(), project.getModules()), inputFiles, outputFile)
            .executeAggregation();
    }
}

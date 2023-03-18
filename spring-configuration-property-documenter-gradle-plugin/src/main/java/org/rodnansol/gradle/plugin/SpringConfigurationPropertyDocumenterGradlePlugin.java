package org.rodnansol.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.rodnansol.gradle.GenerateAndAggregateDocumentsMojo;
import org.rodnansol.gradle.tasks.GenerateDocumentTask;

import java.util.stream.Collectors;

/**
 *
 *
 * @author nandorholozsnyak
 * @author thkadir
 * @since 0.5.0
 */
public class SpringConfigurationPropertyDocumenterGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        GenerateAndAggregateDocumentsMojo generateAndAggregateDocumentsMojo = new GenerateAndAggregateDocumentsMojo();
        generateAndAggregateDocumentsMojo.setProject(project.getName());
        generateAndAggregateDocumentsMojo.setBaseDir(project.getPath());
        generateAndAggregateDocumentsMojo.setName(project.getName());
        generateAndAggregateDocumentsMojo.setModules(project.getAllprojects()
            .stream()
            .map(p -> p.getPath())
            .collect(Collectors.toList()));
        generateAndAggregateDocumentsMojo.setDescription(project.getDescription());

        project.getTasks().register("generateDocument", GenerateDocumentTask.class, generateDocumentTask -> {
            System.out.println("Running");
        });

        project.task("generate")
            .doLast(task -> {
                generateAndAggregateDocumentsMojo.execute();
            });
    }
}

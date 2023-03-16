package org.rodnansol.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.stream.Collectors;

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

        project.task("generate")
            .doLast(task -> {
                generateAndAggregateDocumentsMojo.execute();
            });
    }
}

package org.rodnansol.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.rodnansol.gradle.tasks.GenerateAndAggregateDocumentsTask;
import org.rodnansol.gradle.tasks.GenerateDocumentTask;

/**
 * Class representing the Gradle plugin setup.
 *
 * @author nandorholozsnyak
 * @author thkadir
 * @since 0.5.0
 */
public class SpringConfigurationPropertyDocumenterGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().register("generateDocument", GenerateDocumentTask.class, generateDocumentTask -> {
        });
        project.getTasks().register("generateAndAggregateDocuments", GenerateAndAggregateDocumentsTask.class, generateAndAggregateDocumentsTask -> {
        });
    }
}

package org.rodnansol.core.project.simple;

import org.rodnansol.core.project.Project;
import org.rodnansol.core.project.ProjectType;

import java.io.File;
import java.util.List;

/**
 * Class representing a simple class.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class SimpleProject extends Project {

    public SimpleProject(File basedir, String name) {
        super(basedir, name, ProjectType.SIMPLE);
    }

    @Override
    public List<String> getPossibleMetadataFilePaths() {
        return List.of(
            "/META-INF/spring-configuration-metadata.json",
            "/spring-configuration-metadata.json"
        );
    }
}

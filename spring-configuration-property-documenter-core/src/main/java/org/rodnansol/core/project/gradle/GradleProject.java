package org.rodnansol.core.project.gradle;

import org.rodnansol.core.project.Project;
import org.rodnansol.core.project.ProjectType;

import java.io.File;
import java.util.List;

/**
 * Class representing a Gradle project.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class GradleProject extends Project {

    public GradleProject(File basedir, String name) {
        super(basedir, name, ProjectType.GRADLE);
    }

    public GradleProject(File basedir, String name, List<String> modules) {
        super(basedir, name, ProjectType.GRADLE);
        this.modules = modules;
    }

}

package org.rodnansol.core.util;

import java.io.File;
import java.util.List;

/**
 * Class representing a project.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Project {

    private final File basedir;
    private final String name;
    private final List<String> modules;
    private final ProjectType projectType;

    public Project(File basedir, String name, List<String> modules, ProjectType projectType) {
        this.basedir = basedir;
        this.name = name;
        this.modules = modules;
        this.projectType = projectType;
    }

    public static Project ofMavenProject(File basedir, String name, List<String> modules) {
        return new Project(basedir, name, modules, ProjectType.MAVEN);
    }

    public File getBasedir() {
        return basedir;
    }

    public List<String> getModules() {
        return modules;
    }

    public String getName() {
        return name;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public enum ProjectType {
        MAVEN,
        GRADLE;
    }
}

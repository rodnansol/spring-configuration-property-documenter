package org.rodnansol.core.project;

import org.rodnansol.core.project.gradle.GradleProject;
import org.rodnansol.core.project.maven.MavenProject;
import org.rodnansol.core.project.simple.SimpleProject;

import java.io.File;
import java.util.List;

/**
 * Class acting as a factory for the different project types.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class ProjectFactory {

    private ProjectFactory() {
    }

    /**
     * Creates a {@link MavenProject} instance with a basedir and the module's name.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @return created {@link MavenProject} instance.
     */
    public static MavenProject ofMavenProject(File basedir, String name) {
        return new MavenProject(basedir, name);
    }

    /**
     * Creates a {@link MavenProject} instance with a basedir and the module's name with modules.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @param modules module list.
     * @return created {@link MavenProject} instance.
     */
    public static MavenProject ofMavenProject(File basedir, String name, List<String> modules) {
        return new MavenProject(basedir, name, modules);
    }

    /**
     * Creates a {@link GradleProject} instance with a basedir and the module's name.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @return created {@link GradleProject} instance.
     */
    public static GradleProject ofGradleProject(File basedir, String name) {
        return new GradleProject(basedir, name);
    }

    /**
     * Creates a {@link GradleProject} instance with a basedir and the module's name with modules.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @param modules module list.
     * @return created {@link GradleProject} instance.
     */
    public static GradleProject ofGradleProject(File basedir, String name, List<String> modules) {
        return new GradleProject(basedir, name, modules);
    }

    /**
     * Creates a {@link SimpleProject} instance with a basedir and the module's name.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @return created {@link SimpleProject} instance.
     */
    public static SimpleProject ofSimpleProject(File basedir, String name) {
        return new SimpleProject(basedir, name);
    }

    /**
     * Creates a {@link Project} instance with a basedir and the module's name based on the incoming project type.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @return proper {@link Project} subclass instance.
     */
    public static Project ofType(File basedir, String name, ProjectType projectType) {
        Project project = null;
        switch (projectType) {
            case MAVEN:
                project = ofMavenProject(basedir, name);
                break;
            case GRADLE:
                project = ofGradleProject(basedir, name);
                break;
            case SIMPLE:
                project = ofSimpleProject(basedir, name);
        }
        return project;
    }

    /**
     * Creates a {@link Project} instance with a basedir and the module's name based and modules on the incoming project type.
     *
     * @param basedir base directory for the project.
     * @param name    name of the project.
     * @param modules module list.
     * @return proper {@link Project} subclass instance.
     */
    public static Project ofTypeWithModules(File basedir, String name, List<String> modules, ProjectType projectType) {
        Project project = null;
        switch (projectType) {
            case MAVEN:
                project = ofMavenProject(basedir, name, modules);
                break;
            case GRADLE:
                project = ofGradleProject(basedir, name, modules);
                break;
            case SIMPLE:
                project = ofSimpleProject(basedir, name);
        }
        return project;
    }

}

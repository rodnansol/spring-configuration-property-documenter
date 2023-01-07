package org.rodnansol.core.project;

import org.rodnansol.core.generator.template.TemplateType;

import java.io.File;
import java.util.List;

/**
 * Class representing a project.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public abstract class Project {

    protected final File basedir;
    protected final String name;
    protected final ProjectType projectType;
    protected List<String> modules;

    protected Project(File basedir, String name, ProjectType projectType) {
        this.basedir = basedir;
        this.name = name;
        this.projectType = projectType;
    }

    public File getBasedir() {
        return basedir;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    @Override
    public String toString() {
        return "Project{" +
            "basedir=" + basedir +
            ", name='" + name + '\'' +
            ", modules=" + modules +
            ", projectType=" + projectType +
            '}';
    }

    /**
     * Returns the default output folder based on the {@link Project} instance and module name (in multi-module setup).
     * <p>
     * For example: <b>multi-module-parent/multi-module-a/target/property-docs</b>
     *
     * @param moduleName module's name.
     * @return output folder based on the incoming parameters.
     */
    public String getDefaultOutputFolder(String moduleName) {
        return null;
    }

    /**
     * Returns the default output folder based on the {@link Project} instance.
     * <p>
     * For example: <b>module-a/target/property-docs</b>
     *
     * @return output folder based on the incoming parameter.
     */
    public String getDefaultOutputFolder() {
        return null;
    }

    /**
     * Returns the output file path based on  the {@link Project} instance and template type value.
     *
     * <p>
     * For example: <b>multi-module-parent/target/property-docs/multi-module-parent-property-docs-aggregated.md</b>
     *
     * @param templateType type of the template, the extension will be determined based on this.
     * @return target file path based on the project instance and template type.
     */
    public String getDefaultTargetFilePath(TemplateType templateType) {
        return null;
    }

    /**
     * Returns the output file path based on  the {@link Project} instance and template type value.
     *
     * <p>
     * For example: <b>module-a/target/property-docs/module-a-property-docs.md</b>
     *
     * @param extension extension (like .md, .adoc etc)
     * @return target file path based on the incoming parameters.
     */
    public String getDefaultAggregatedTargetFilePath(String extension) {
        return null;
    }

    /**
     * Returns the list of the possible metadata file paths inside a project.
     *
     * @return list of the possible metadata file paths.
     */
    public List<String> getPossibleMetadataFilePaths() {
        return List.of();
    }

}

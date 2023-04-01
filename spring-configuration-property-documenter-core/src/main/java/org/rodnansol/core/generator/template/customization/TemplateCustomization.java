package org.rodnansol.core.generator.template.customization;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Class represents a template customization object.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public interface TemplateCustomization {

    /**
     * Returns the 'Table of Contents' section title.
     *
     * @since 0.4.0
     */
    String getTocTitle();

    /**
     * If the unknown group should be included or not in the final rendered document or not.
     *
     * @since 0.3.0
     */
    boolean isIncludeUnknownGroup();

    /**
     * Returns the title for the unknown group.
     *
     * @since 0.4.0
     */
    String getUnknownGroupLocalization();

    /**
     * If the properties should be converted to their environment variable representation to have a quicker way to copy and paste them.
     *
     * @since 0.4.0
     */
    boolean isIncludeEnvFormat();

    /**
     * If the generation date should be rendered into the document or not.
     *
     * @since 0.4.0
     */
    boolean isIncludeGenerationDate();

    /**
     * If empty groups must be removed from the final document or not.
     *
     * @since 0.4.0
     */
    boolean isRemoveEmptyGroups();

    /**
     * Returns the set locale.
     *
     * @since 0.4.0
     */
    String getLocale();

    /**
     * Returns the template content customization.
     *
     * @since 0.6.0
     */
    @NonNull
    ContentCustomization getContentCustomization();

    /**
     * Sets the template content customization.
     *
     * @since 0.6.0
     */
    void setContentCustomization(@NonNull ContentCustomization contentCustomization);

}

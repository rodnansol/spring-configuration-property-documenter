package org.rodnansol.core.generator.template.customization;

/**
 * Class represents a template customization object.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public interface TemplateCustomization {

    /**
     * If the unknown group should be included or not in the final rendered document or not.
     *
     * @since 0.3.0
     */
    boolean isIncludeUnknownGroup();

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

}

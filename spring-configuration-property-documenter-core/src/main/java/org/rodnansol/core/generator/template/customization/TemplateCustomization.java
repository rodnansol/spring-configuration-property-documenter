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
     * @since 0.2.5
     */
    boolean isIncludeUnknownGroup();

}

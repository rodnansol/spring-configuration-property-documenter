package org.rodnansol.gradle.tasks.customization;

/**
 * Enumeration collecting all the available template modes.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public enum TemplateMode {

    /**
     * This template mode has detailed information about the property groups and its properties.
     *
     * @since 0.1.0
     */
    STANDARD,

    /**
     * This template mode is only containing the properties without any information about the property groups.
     *
     * @since 0.6.0
     */
    COMPACT
}

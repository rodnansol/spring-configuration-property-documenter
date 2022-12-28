package org.rodnansol.core.util;

/**
 * Class containing constants related to the plugin.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public final class PluginConstants {

    /**
     * Default folder name where the generated files will be stored.
     */
    public static final String DEFAULT_TARGET_FOLDER = "property-docs";

    /**
     * Default output directory for the documents.
     */
    public static final String DEFAULT_OUTPUT_DIRECTORY = "%s/target/" + DEFAULT_TARGET_FOLDER;

    /**
     * Default target file path, must be formatted.
     */
    public static final String DEFAULT_TARGET_FILE_PATH = "%s/%s-property-docs%s";
    
    /**
     * Default aggregated target file path, must be formatted.
     */
    public static final String DEFAULT_AGGREGATED_FILE_PATH = "%s/%s-property-docs-aggregated.%s";

    private PluginConstants() {
    }
}

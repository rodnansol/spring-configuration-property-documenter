package org.rodnansol.core.generator.template.customization;

/**
 * Class represents a template customization object.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public abstract class AbstractTemplateCustomization implements TemplateCustomization {

    /**
     * If the header should be enabled or not.
     * @since 0.2.0
     */
    private boolean headerEnabled = true;

    /**
     * Should the "Table of Contents" enabled or not.
     *
     * @since 0.2.0
     */
    private boolean tableOfContentsEnabled = true;

    public boolean isHeaderEnabled() {
        return headerEnabled;
    }

    public void setHeaderEnabled(boolean headerEnabled) {
        this.headerEnabled = headerEnabled;
    }

    public boolean isTableOfContentsEnabled() {
        return tableOfContentsEnabled;
    }

    public void setTableOfContentsEnabled(boolean tableOfContentsEnabled) {
        this.tableOfContentsEnabled = tableOfContentsEnabled;
    }
}

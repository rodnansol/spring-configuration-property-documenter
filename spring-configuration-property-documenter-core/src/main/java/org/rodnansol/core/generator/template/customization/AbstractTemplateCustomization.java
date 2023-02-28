package org.rodnansol.core.generator.template.customization;

import java.util.Objects;

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
    protected boolean headerEnabled = true;

    /**
     * Should the "Table of Contents" enabled or not.
     *
     * @since 0.2.0
     */
    protected boolean tableOfContentsEnabled = true;

    /**
     * If the 'Without type'/Unknown group should be included or not.
     *
     * @since 0.2.5
     */
    protected boolean includeUnknownGroup = true;

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

    public boolean isIncludeUnknownGroup() {
        return includeUnknownGroup;
    }

    public void setIncludeUnknownGroup(boolean includeUnknownGroup) {
        this.includeUnknownGroup = includeUnknownGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTemplateCustomization that = (AbstractTemplateCustomization) o;
        return headerEnabled == that.headerEnabled && tableOfContentsEnabled == that.tableOfContentsEnabled && includeUnknownGroup == that.includeUnknownGroup;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerEnabled, tableOfContentsEnabled, includeUnknownGroup);
    }
}

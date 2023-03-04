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
     * If the 'Unknown group' should be included or not.
     *
     * @since 0.3.0
     */
    protected boolean includeUnknownGroup = true;

    /**
     * If the properties should be converted to their environment variable representation to have a quicker way to copy and paste them.
     *
     * @since 0.4.0
     */
    protected boolean includeEnvFormat = false;

    /**
     * If the generation date should be rendered into the document or not.
     *
     * @since 0.4.0
     */
    protected boolean includeGenerationDate = true;

    /**
     * If empty groups must be removed from the final document or not.
     *
     * @since 0.4.0
     */
    protected boolean removeEmptyGroups = false;

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

    @Override
    public boolean isIncludeUnknownGroup() {
        return includeUnknownGroup;
    }

    public void setIncludeUnknownGroup(boolean includeUnknownGroup) {
        this.includeUnknownGroup = includeUnknownGroup;
    }

    @Override
    public boolean isIncludeEnvFormat() {
        return includeEnvFormat;
    }

    public void setIncludeEnvFormat(boolean includeEnvFormat) {
        this.includeEnvFormat = includeEnvFormat;
    }

    @Override
    public boolean isIncludeGenerationDate() {
        return includeGenerationDate;
    }

    public void setIncludeGenerationDate(boolean includeGenerationDate) {
        this.includeGenerationDate = includeGenerationDate;
    }

    @Override
    public boolean isRemoveEmptyGroups() {
        return removeEmptyGroups;
    }

    public void setRemoveEmptyGroups(boolean removeEmptyGroups) {
        this.removeEmptyGroups = removeEmptyGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTemplateCustomization that = (AbstractTemplateCustomization) o;
        return headerEnabled == that.headerEnabled && tableOfContentsEnabled == that.tableOfContentsEnabled && includeUnknownGroup == that.includeUnknownGroup && includeEnvFormat == that.includeEnvFormat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerEnabled, tableOfContentsEnabled, includeUnknownGroup, includeEnvFormat);
    }
}

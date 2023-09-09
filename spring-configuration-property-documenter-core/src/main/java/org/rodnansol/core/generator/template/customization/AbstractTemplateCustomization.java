package org.rodnansol.core.generator.template.customization;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.rodnansol.core.generator.template.TemplateMode;
import org.rodnansol.core.generator.template.data.PropertyGroupConstants;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class represents a template customization object.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public abstract class AbstractTemplateCustomization implements TemplateCustomization, Serializable {

    /**
     * Table of Contents title.
     *
     * @since 0.4.0
     */
    protected String tocTitle;

    /**
     * If the header should be enabled or not.
     *
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
     * Returns the title for the unknown group.
     *
     * @since 0.4.0
     */
    protected String unknownGroupLocalization = PropertyGroupConstants.UNKNOWN_GROUP;

    /**
     * If the properties should be converted to their environment variable representation to have a quicker way to copy and paste them.
     *
     * @since 0.4.0
     */
    @Deprecated(forRemoval = true, since = "0.6.0")
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

    /**
     * Locale to be used during the i18n process.
     *
     * @since 0.4.0
     */
    protected String locale;

    /**
     * Field storing customization options for the content template.
     *
     * @since 0.6.0
     */
    protected ContentCustomization contentCustomization = new ContentCustomization();

    /**
     * Controls the template mode.
     * <p>
     * By default, the mode is STANDARD to provide backward compatibility.
     *
     * @since 0.6.0
     */
    protected TemplateMode templateMode = TemplateMode.STANDARD;

    public String getTocTitle() {
        return tocTitle;
    }

    public void setTocTitle(String tocTitle) {
        this.tocTitle = tocTitle;
    }

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
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String getUnknownGroupLocalization() {
        return unknownGroupLocalization;
    }

    public void setUnknownGroupLocalization(String unknownGroupLocalization) {
        this.unknownGroupLocalization = unknownGroupLocalization;
    }

    @NonNull
    @Override
    public ContentCustomization getContentCustomization() {
        return contentCustomization;
    }

    @Override
    public void setContentCustomization(@NonNull ContentCustomization contentCustomization) {
        this.contentCustomization = contentCustomization;
    }

    @Override
    public TemplateMode getTemplateMode() {
        return templateMode;
    }

    @Override
    public void setTemplateMode(TemplateMode templateMode) {
        this.templateMode = templateMode;
    }

    @Override
    public String toString() {
        return "AbstractTemplateCustomization{" +
            "tocTitle='" + tocTitle + '\'' +
            ", headerEnabled=" + headerEnabled +
            ", tableOfContentsEnabled=" + tableOfContentsEnabled +
            ", includeUnknownGroup=" + includeUnknownGroup +
            ", unknownGroupLocalization='" + unknownGroupLocalization + '\'' +
            ", includeEnvFormat=" + includeEnvFormat +
            ", includeGenerationDate=" + includeGenerationDate +
            ", removeEmptyGroups=" + removeEmptyGroups +
            ", locale='" + locale + '\'' +
            ", contentCustomization=" + contentCustomization +
            ", templateMode=" + templateMode +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTemplateCustomization that = (AbstractTemplateCustomization) o;
        return headerEnabled == that.headerEnabled && tableOfContentsEnabled == that.tableOfContentsEnabled && includeUnknownGroup == that.includeUnknownGroup && includeEnvFormat == that.includeEnvFormat && includeGenerationDate == that.includeGenerationDate && removeEmptyGroups == that.removeEmptyGroups && Objects.equals(tocTitle, that.tocTitle) && Objects.equals(unknownGroupLocalization, that.unknownGroupLocalization) && Objects.equals(locale, that.locale) && Objects.equals(contentCustomization, that.contentCustomization) && templateMode == that.templateMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tocTitle, headerEnabled, tableOfContentsEnabled, includeUnknownGroup, unknownGroupLocalization, includeEnvFormat, includeGenerationDate, removeEmptyGroups, locale, contentCustomization, templateMode);
    }
}

package org.rodnansol.gradle.tasks.customization;

import edu.umd.cs.findbugs.annotations.NonNull;
import groovy.lang.Closure;
import org.rodnansol.core.generator.template.data.PropertyGroupConstants;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Class represents a template customization object.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public abstract class AbstractTemplateCustomization implements Serializable {

    /**
     * Table of Contents title.
     *
     * @since 0.6.0
     */
    protected String tocTitle;

    /**
     * If the header should be enabled or not.
     *
     * @since 0.6.0
     */
    protected boolean headerEnabled = true;

    /**
     * Should the "Table of Contents" enabled or not.
     *
     * @since 0.6.0
     */
    protected boolean tableOfContentsEnabled = true;

    /**
     * If the 'Unknown group' should be included or not.
     *
     * @since 0.6.0
     */
    protected boolean includeUnknownGroup = true;

    /**
     * Returns the title for the unknown group.
     *
     * @since 0.6.0
     */
    protected String unknownGroupLocalization = PropertyGroupConstants.UNKNOWN_GROUP;

    /**
     * If the generation date should be rendered into the document or not.
     *
     * @since 0.6.0
     */
    protected boolean includeGenerationDate = true;

    /**
     * If empty groups must be removed from the final document or not.
     *
     * @since 0.6.0
     */
    protected boolean removeEmptyGroups = false;

    /**
     * Locale to be used during the i18n process.
     *
     * @since 0.6.0
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

    public boolean isIncludeUnknownGroup() {
        return includeUnknownGroup;
    }

    public void setIncludeUnknownGroup(boolean includeUnknownGroup) {
        this.includeUnknownGroup = includeUnknownGroup;
    }

    public boolean isIncludeGenerationDate() {
        return includeGenerationDate;
    }

    public void setIncludeGenerationDate(boolean includeGenerationDate) {
        this.includeGenerationDate = includeGenerationDate;
    }

    public boolean isRemoveEmptyGroups() {
        return removeEmptyGroups;
    }

    public void setRemoveEmptyGroups(boolean removeEmptyGroups) {
        this.removeEmptyGroups = removeEmptyGroups;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUnknownGroupLocalization() {
        return unknownGroupLocalization;
    }

    public void setUnknownGroupLocalization(String unknownGroupLocalization) {
        this.unknownGroupLocalization = unknownGroupLocalization;
    }

    @NonNull
    public ContentCustomization getContentCustomization() {
        return contentCustomization;
    }

    public void setContentCustomization(@NonNull ContentCustomization contentCustomization) {
        this.contentCustomization = contentCustomization;
    }

    public TemplateMode getTemplateMode() {
        return templateMode;
    }

    public void setTemplateMode(TemplateMode templateMode) {
        this.templateMode = templateMode;
    }

    /**
     * DSL entry point for the {@link AbstractTemplateCustomization#contentCustomization} field.
     */
    public void contentCustomization(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(contentCustomization);
        closure.call();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTemplateCustomization that = (AbstractTemplateCustomization) o;
        return headerEnabled == that.headerEnabled && tableOfContentsEnabled == that.tableOfContentsEnabled && includeUnknownGroup == that.includeUnknownGroup && includeGenerationDate == that.includeGenerationDate && removeEmptyGroups == that.removeEmptyGroups && templateMode == that.templateMode && Objects.equals(tocTitle, that.tocTitle) && Objects.equals(unknownGroupLocalization, that.unknownGroupLocalization) && Objects.equals(locale, that.locale) && Objects.equals(contentCustomization, that.contentCustomization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tocTitle, headerEnabled, tableOfContentsEnabled, includeUnknownGroup, unknownGroupLocalization, includeGenerationDate, removeEmptyGroups, locale, contentCustomization, templateMode);
    }

    @Override
    public String toString() {
        return new StringJoiner(",\n\t", AbstractTemplateCustomization.class.getSimpleName() + "[", "]")
            .add("tocTitle='" + tocTitle + "'")
            .add("headerEnabled=" + headerEnabled)
            .add("tableOfContentsEnabled=" + tableOfContentsEnabled)
            .add("includeUnknownGroup=" + includeUnknownGroup)
            .add("unknownGroupLocalization='" + unknownGroupLocalization + "'")
            .add("includeGenerationDate=" + includeGenerationDate)
            .add("removeEmptyGroups=" + removeEmptyGroups)
            .add("locale='" + locale + "'")
            .add("contentCustomization=" + contentCustomization)
            .add("templateMode=" + templateMode)
            .toString();
    }
}

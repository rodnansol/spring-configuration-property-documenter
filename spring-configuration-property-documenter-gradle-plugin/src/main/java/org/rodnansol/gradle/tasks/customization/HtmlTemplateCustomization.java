package org.rodnansol.gradle.tasks.customization;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Class representing HTML customizations.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class HtmlTemplateCustomization extends AbstractTemplateCustomization {

    /**
     * Background color.
     * @since 0.6.0
     */
    private String backgroundColor = "#7db04b";

    /**
     * Link color.
     * @since 0.6.0
     */
    private String linkColor = "black";

    /**
     * Collapsible hover color.
     * @since 0.6.0
     */
    private String collapsibleHoverColor = "#96d95c";

    /**
     * Code color.
     * @since 0.6.0
     */
    private String codeColor = "#eeeeee";

    /**
     * Even table row color.
     * @since 0.6.0
     */
    private String evenTableRowColor = "#f3f3f3";

    /**
     * Last table row color.
     * @since 0.6.0
     */
    private String lastTableRowColor = "#009879";

    /**
     * Table row color.
     * @since 0.6.0
     */
    private String tableRowColor = "#ffffff";

    /**
     * Table row border color.
     * @since 0.6.0
     */
    private String tableRowBorderColor = "#dddddd";

    /**
     * Text color.
     * @since 0.6.0
     */
    private String textColor = "black";

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(String linkColor) {
        this.linkColor = linkColor;
    }

    public String getCollapsibleHoverColor() {
        return collapsibleHoverColor;
    }

    public void setCollapsibleHoverColor(String collapsibleHoverColor) {
        this.collapsibleHoverColor = collapsibleHoverColor;
    }

    public String getCodeColor() {
        return codeColor;
    }

    public void setCodeColor(String codeColor) {
        this.codeColor = codeColor;
    }

    public String getEvenTableRowColor() {
        return evenTableRowColor;
    }

    public void setEvenTableRowColor(String evenTableRowColor) {
        this.evenTableRowColor = evenTableRowColor;
    }

    public String getLastTableRowColor() {
        return lastTableRowColor;
    }

    public void setLastTableRowColor(String lastTableRowColor) {
        this.lastTableRowColor = lastTableRowColor;
    }

    public String getTableRowColor() {
        return tableRowColor;
    }

    public void setTableRowColor(String tableRowColor) {
        this.tableRowColor = tableRowColor;
    }

    public String getTableRowBorderColor() {
        return tableRowBorderColor;
    }

    public void setTableRowBorderColor(String tableRowBorderColor) {
        this.tableRowBorderColor = tableRowBorderColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HtmlTemplateCustomization that = (HtmlTemplateCustomization) o;
        return Objects.equals(backgroundColor, that.backgroundColor) && Objects.equals(linkColor, that.linkColor) && Objects.equals(collapsibleHoverColor, that.collapsibleHoverColor) && Objects.equals(codeColor, that.codeColor) && Objects.equals(evenTableRowColor, that.evenTableRowColor) && Objects.equals(lastTableRowColor, that.lastTableRowColor) && Objects.equals(tableRowColor, that.tableRowColor) && Objects.equals(tableRowBorderColor, that.tableRowBorderColor) && Objects.equals(textColor, that.textColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), backgroundColor, linkColor, collapsibleHoverColor, codeColor, evenTableRowColor, lastTableRowColor, tableRowColor, tableRowBorderColor, textColor);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HtmlTemplateCustomization.class.getSimpleName() + "[", "]")
            .add("backgroundColor='" + backgroundColor + "'")
            .add("linkColor='" + linkColor + "'")
            .add("collapsibleHoverColor='" + collapsibleHoverColor + "'")
            .add("codeColor='" + codeColor + "'")
            .add("evenTableRowColor='" + evenTableRowColor + "'")
            .add("lastTableRowColor='" + lastTableRowColor + "'")
            .add("tableRowColor='" + tableRowColor + "'")
            .add("tableRowBorderColor='" + tableRowBorderColor + "'")
            .add("textColor='" + textColor + "'")
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

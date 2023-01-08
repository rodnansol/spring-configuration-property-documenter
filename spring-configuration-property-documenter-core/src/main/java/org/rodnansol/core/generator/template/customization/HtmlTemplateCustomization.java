package org.rodnansol.core.generator.template.customization;

/**
 * Class representing HTML customizations.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public class HtmlTemplateCustomization extends AbstractTemplateCustomization {

    /**
     * Background color.
     * @since 0.2.0
     */
    private String backgroundColor = "#7db04b";

    /**
     * Link color.
     * @since 0.2.0
     */
    private String linkColor = "black";

    /**
     * Collapsible hover color.
     * @since 0.2.0
     */
    private String collapsibleHoverColor = "#96d95c";

    /**
     * Code color.
     * @since 0.2.0
     */
    private String codeColor = "#eeeeee";

    /**
     * Even table row color.
     * @since 0.2.0
     */
    private String evenTableRowColor = "#f3f3f3";

    /**
     * Last table row color.
     * @since 0.2.0
     */
    private String lastTableRowColor = "#009879";

    /**
     * Table row color.
     * @since 0.2.0
     */
    private String tableRowColor = "#ffffff";

    /**
     * Table row border color.
     * @since 0.2.0
     */
    private String tableRowBorderColor = "#dddddd";

    /**
     * Text color.
     * @since 0.2.0
     */
    private String textColor = "white";

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
}

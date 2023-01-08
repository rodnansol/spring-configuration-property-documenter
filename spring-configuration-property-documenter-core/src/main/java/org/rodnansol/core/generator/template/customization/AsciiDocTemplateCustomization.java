package org.rodnansol.core.generator.template.customization;

/**
 * Class representing extra customizations for the AsciiDoc template.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public class AsciiDocTemplateCustomization extends AbstractTemplateCustomization {

    /**
     * Table of Contents title.
     * @since 0.2.0
     */
    private String tocTitle = "Table of Contents";

    /**
     * Placement of the "Table of Contents"
     * @since 0.2.0
     */
    private TocPlacement tocPlacement = TocPlacement.AUTO;

    /**
     * Table of contents levels.
     * @since 0.2.0
     */
    private int tocLevels = 4;

    public String getTocTitle() {
        return tocTitle;
    }

    public void setTocTitle(String tocTitle) {
        this.tocTitle = tocTitle;
    }

    public TocPlacement getTocPlacement() {
        return tocPlacement;
    }

    public void setTocPlacement(TocPlacement tocPlacement) {
        this.tocPlacement = tocPlacement;
    }

    public int getTocLevels() {
        return tocLevels;
    }

    public void setTocLevels(int tocLevels) {
        this.tocLevels = tocLevels;
    }

    public enum TocPlacement {
        AUTO,
        LEFT,
        RIGHT;

        public String toLowerCase() {
            return this.name().toLowerCase();
        }
    }

}

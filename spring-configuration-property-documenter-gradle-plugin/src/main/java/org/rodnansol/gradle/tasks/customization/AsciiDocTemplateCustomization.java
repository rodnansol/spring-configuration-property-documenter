package org.rodnansol.gradle.tasks.customization;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Class representing extra customizations for the AsciiDoc template.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class AsciiDocTemplateCustomization extends AbstractTemplateCustomization {

    /**
     * Placement of the "Table of Contents"
     * @since 0.6.0
     */
    private TocPlacement tocPlacement = TocPlacement.AUTO;

    /**
     * Table of contents levels.
     * @since 0.6.0
     */
    private int tocLevels = 4;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AsciiDocTemplateCustomization that = (AsciiDocTemplateCustomization) o;
        return tocLevels == that.tocLevels && tocPlacement == that.tocPlacement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tocPlacement, tocLevels);
    }

    @Override
    public String toString() {
        return new StringJoiner(",\n\t", AsciiDocTemplateCustomization.class.getSimpleName() + "[", "]")
            .add("tocPlacement=" + tocPlacement)
            .add("tocLevels=" + tocLevels)
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

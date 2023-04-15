package org.rodnansol.core.generator.template.customization;

import org.rodnansol.core.generator.template.TemplateType;

/**
 * Class representing a factory for the template customization objects.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public class TemplateCustomizationFactory {

    private TemplateCustomizationFactory() {
    }

    /**
     * Returns an empty {@link TemplateCustomization} instance for the given template type.
     */
    public static TemplateCustomization getDefaultTemplateCustomizationByType(TemplateType templateType) {
        switch (templateType) {
            case MARKDOWN:
                return new MarkdownTemplateCustomization();
            case ADOC:
                return new AsciiDocTemplateCustomization();
            case HTML:
                return new HtmlTemplateCustomization();
            case XML:
                return new XmlTemplateCustomization();
            default:
                throw new IllegalArgumentException("Unknown template type.");
        }
    }

}

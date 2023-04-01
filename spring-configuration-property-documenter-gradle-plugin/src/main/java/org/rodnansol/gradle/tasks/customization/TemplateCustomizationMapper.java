package org.rodnansol.gradle.tasks.customization;

import org.mapstruct.Mapper;

/**
 * Mapper class that deals with the mapping between the core and Gradle based template customization classes.
 */
@Mapper
public interface TemplateCustomizationMapper {

    org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization toAsciiDoc(org.rodnansol.gradle.tasks.customization.AsciiDocTemplateCustomization asciiDocTemplateCustomization);

    org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization toMarkdown(org.rodnansol.gradle.tasks.customization.MarkdownTemplateCustomization markdownTemplateCustomization);

    org.rodnansol.core.generator.template.customization.HtmlTemplateCustomization toHtml(org.rodnansol.gradle.tasks.customization.HtmlTemplateCustomization htmlTemplateCustomization);

    org.rodnansol.core.generator.template.customization.XmlTemplateCustomization toXml(org.rodnansol.gradle.tasks.customization.XmlTemplateCustomization xmlTemplateCustomization);

    org.rodnansol.core.generator.template.customization.ContentCustomization toContentCustomization(org.rodnansol.gradle.tasks.customization.ContentCustomization contentCustomization);

}

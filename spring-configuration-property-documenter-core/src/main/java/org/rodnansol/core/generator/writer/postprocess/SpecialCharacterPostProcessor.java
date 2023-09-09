package org.rodnansol.core.generator.writer.postprocess;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import org.rodnansol.core.generator.template.customization.AsciiDocTemplateCustomization;
import org.rodnansol.core.generator.template.customization.MarkdownTemplateCustomization;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.generator.template.customization.XmlTemplateCustomization;
import org.rodnansol.core.generator.template.data.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

/**
 * The SpecialCharacterPostProcessor class is responsible for processing special characters in property descriptions and default values of property groups.
 * It implements the PropertyGroupPostProcessor interface.
 * <p>
 * The class contains a static final map SPECIAL_CHARACTER_MAP that maps each TemplateCustomization class to a map of special characters and their replacements.
 * Currently, three TemplateCustomization classes are supported: MarkdownTemplateCustomization, AsciiDocTemplateCustomization, and XmlTemplateCustomization.
 * Each TemplateCustomization class has its own set of special characters and replacements.
 * <p>
 * The SpecialCharacterPostProcessor class provides a postProcess() method that takes a PostProcessPropertyGroupsCommand object as a parameter.
 * It retrieves the template customization from the command object and the corresponding special character map from the SPECIAL_CHARACTER_MAP.
 * Then, it iterates over each property in the property groups and applies the special character replacements to the description and default value of each property.
 */
class SpecialCharacterPostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialCharacterPostProcessor.class);

    private static final Map<Class<? extends TemplateCustomization>, Map<String, String>> SPECIAL_CHARACTER_MAP = ofEntries(
        entry(MarkdownTemplateCustomization.class, ofEntries(
            entry("|", "&#124")
        )),
        entry(AsciiDocTemplateCustomization.class, ofEntries(
            entry("|", "\\|")
        )),
        entry(XmlTemplateCustomization.class, ofEntries(
            entry("<", "&lt;"),
            entry(">", "&gt;"),
            entry("&", "&amp;"),
            entry("'", "&apos;"),
            entry("\"", "&quot;")
        ))
    );

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        TemplateCustomization templateCustomization = command.getTemplateCustomization();
        Map<String, String> specialCharacterMap = SPECIAL_CHARACTER_MAP.get(templateCustomization.getClass());
        if (specialCharacterMap != null) {
            LOGGER.debug("Removing special characters from the property descriptions and default values.");
            command.getPropertyGroups()
                .stream()
                .flatMap(propertyGroup -> propertyGroup.getProperties().stream())
                .filter(property -> StringUtils.isNotBlank(property.getDescription()) || StringUtils.isNotBlank(property.getDefaultValue()))
                .forEach(property -> replaceSpecialCharacters(property, specialCharacterMap));
        }
    }

    private void replaceSpecialCharacters(Property property, Map<String, String> specialCharacterMap) {
        specialCharacterMap.forEach((character, replacement) -> {
            if (Objects.nonNull(property.getDescription()))
                property.setDescription(property.getDescription().replace(character, replacement));
            if (Objects.nonNull(property.getDefaultValue()))
                property.setDefaultValue(property.getDefaultValue().replace(character, replacement));
        });
    }
}

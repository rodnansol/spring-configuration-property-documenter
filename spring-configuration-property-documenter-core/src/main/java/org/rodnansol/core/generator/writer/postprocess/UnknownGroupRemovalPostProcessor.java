package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Post processor that removes the Unknown group if needed
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
class UnknownGroupRemovalPostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownGroupRemovalPostProcessor.class);

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        List<PropertyGroup> propertyGroups = command.getPropertyGroups();
        TemplateCustomization templateCustomization = command.getTemplateCustomization();
        if (templateCustomization != null && !templateCustomization.isIncludeUnknownGroup()) {
            LOGGER.debug("Removing 'Unknown' group");
            propertyGroups.removeIf(PropertyGroup::isUnknownGroup);
        }
    }
}

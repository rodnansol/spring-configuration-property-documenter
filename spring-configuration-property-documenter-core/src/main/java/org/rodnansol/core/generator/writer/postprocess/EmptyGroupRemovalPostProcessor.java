package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Post processor that removes empty groups if needed
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
class EmptyGroupRemovalPostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyGroupRemovalPostProcessor.class);

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        List<PropertyGroup> propertyGroups = command.getPropertyGroups();
        TemplateCustomization templateCustomization = command.getTemplateCustomization();
        if (templateCustomization != null && templateCustomization.isRemoveEmptyGroups()) {
            LOGGER.debug("Removing empty groups...");
            if (propertyGroups != null) {
                propertyGroups.removeIf(group -> group.getProperties() == null || group.getProperties().isEmpty());
            }
        }
    }
}

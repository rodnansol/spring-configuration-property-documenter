package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.data.PropertyGroupConstants;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Post processor that renames the unknown group if needed.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
class UnknownGroupRenamePostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownGroupRenamePostProcessor.class);

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        List<PropertyGroup> propertyGroups = command.getPropertyGroups();
        TemplateCustomization templateCustomization = command.getTemplateCustomization();
        if (templateCustomization != null) {
            String unknownGroupLocalization = templateCustomization.getUnknownGroupLocalization();
            if (!PropertyGroupConstants.UNKNOWN_GROUP.equals(unknownGroupLocalization)) {
                LOGGER.debug("Renaming unknown group to:[{}]", unknownGroupLocalization);
                if (propertyGroups != null) {
                    propertyGroups.stream().filter(PropertyGroup::isUnknownGroup)
                        .forEach(group -> group.setGroupName(unknownGroupLocalization));
                }
            }
        }
    }
}

package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.PropertyGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Post processor that deals with the include and exclude list for the properties.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
class IncludeExcludePropertyPostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncludeExcludePropertyPostProcessor.class);

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        List<PropertyGroup> propertyGroups = command.getPropertyGroups();
        List<String> includeList = command.getIncludedProperties();
        List<String> excludeList = command.getExcludedProperties();
        LOGGER.debug("Filtering incoming property group's properties with include list:[{}] and exculde list:[{}]", includeList, excludeList);
        if (includeList != null && !includeList.isEmpty()) {
            for (PropertyGroup propertyGroup : propertyGroups) {
                if (propertyGroup.getProperties() != null) {
                    propertyGroup.getProperties().removeIf(property -> !includeList.contains(property.getFqName()));
                }
            }
            return;
        }
        if (excludeList != null && !excludeList.isEmpty()) {
            for (PropertyGroup propertyGroup : propertyGroups) {
                if (propertyGroup.getProperties() != null) {
                    propertyGroup.getProperties().removeIf(property -> excludeList.contains(property.getFqName()));
                }
            }
        }
    }
}

package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Post processor that deals with the include and exclude list for the groups.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
class IncludeExcludeGroupPostProcessor implements PropertyGroupPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncludeExcludeGroupPostProcessor.class);

    @Override
    public void postProcess(PostProcessPropertyGroupsCommand command) {
        List<PropertyGroup> propertyGroups = command.getPropertyGroups();
        List<String> includeList = command.getIncludedGroups();
        List<String> excludeList = command.getExcludedGroups();
        LOGGER.debug("Filtering incoming property group list with include list:[{}] and exclude list:[{}]", includeList, excludeList);
        if (includeList != null && !includeList.isEmpty() && propertyGroups != null) {
            propertyGroups.removeIf(group -> !includeList.contains(group.getGroupName()));
            return;
        }
        if (excludeList != null && !excludeList.isEmpty() && propertyGroups != null) {
            propertyGroups.removeIf(group -> excludeList.contains(group.getGroupName()));
        }
    }
}

package org.rodnansol.core.generator.writer;

import org.rodnansol.core.generator.template.PropertyGroup;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class that deals with the filtering of the property group lists.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
public class PropertyGroupFilterService {

    public static final PropertyGroupFilterService INSTANCE = new PropertyGroupFilterService();
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyGroupFilterService.class);

    /**
     * Post process the incoming property groups with the different attributes.
     * <p>
     * Applying include or exclusion lists.
     *
     * @param command command that contains all the attributes that is needed for the process.
     */
    public void postProcessPropertyGroups(PostProcessPropertyGroupsCommand command) {
        try {
            List<PropertyGroup> propertyGroups = command.getPropertyGroups();
            TemplateCustomization templateCustomization = command.getTemplateCustomization();
            removeUnknownGroupIfNeeded(templateCustomization, command.getPropertyGroups());
            filterPropertyGroups(propertyGroups, command.getIncludedGroups(), command.getExcludedGroups());
            filterPropertyGroupProperties(propertyGroups, command.getIncludedProperties(), command.getExcludedProperties());
            removeEmptyGroupsIfNeeded(templateCustomization, propertyGroups);
        } catch (Exception e) {
            LOGGER.warn("Error during filtering the property groups and properties, no filtering logic will be applied, please check the logs.", e);
        }
    }

    /**
     * Removes empty groups if needed.
     *
     * @param templateCustomization object that is able to tell the predicate.
     * @param propertyGroups        groups to be checked.
     */
    void removeEmptyGroupsIfNeeded(TemplateCustomization templateCustomization, List<PropertyGroup> propertyGroups) {
        if (templateCustomization != null && templateCustomization.isRemoveEmptyGroups()) {
            removeEmptyGroups(propertyGroups);
        }
    }

    /**
     * Removes the 'Unknown group' if needed.
     *
     * @param templateCustomization object that is able to tell the predicate.
     * @param propertyGroups        groups to be checked.
     */
    void removeUnknownGroupIfNeeded(TemplateCustomization templateCustomization, List<PropertyGroup> propertyGroups) {
        if (templateCustomization != null && !templateCustomization.isIncludeUnknownGroup()) {
            LOGGER.debug("Removing 'Unknown' group");
            propertyGroups.removeIf(PropertyGroup::isUnknownGroup);
        }
    }

    /**
     * Removes groups from the list where the property list is empty.
     *
     * @param propertyGroups list of properties to be filtered.
     */
    void removeEmptyGroups(List<PropertyGroup> propertyGroups) {
        LOGGER.debug("Removing empty groups...");
        if (propertyGroups != null) {
            propertyGroups.removeIf(group -> group.getProperties() == null || group.getProperties().isEmpty());
        }
    }

    /**
     * Filter the incoming property groups based on the incoming include and exclude lists.
     * <p>
     * First if the include list is not empty, then it will remove all keys which are not in the include list.
     * <p>
     * If the exclude list is not empty, then it will remove all keys that are in this list.
     * <p>
     * The two lists are mutually exclusive and the include list is having a bigger priority.
     *
     * @param propertyGroups list of properties to be filtered.
     * @param includeList    list of the included keys.
     * @param excludeList    list of the excluded keys.
     */
    void filterPropertyGroups(List<PropertyGroup> propertyGroups, List<String> includeList, List<String> excludeList) {
        LOGGER.debug("Filtering incoming property group list with include list:[{}] and exclude list:[{}]", includeList, excludeList);
        if (includeList != null && !includeList.isEmpty() && propertyGroups != null) {
            propertyGroups.removeIf(group -> !includeList.contains(group.getGroupName()));
            return;
        }
        if (excludeList != null && !excludeList.isEmpty() && propertyGroups != null) {
            propertyGroups.removeIf(group -> excludeList.contains(group.getGroupName()));
        }
    }

    /**
     * Filter the incoming property group properties based on the incoming include and exclude lists.
     * <p>
     * First if the include list is not empty, then it will remove all keys which are not in the include list.
     * <p>
     * If the exclude list is not empty, then it will remove all keys that are in this list.
     * <p>
     * The two lists are mutually exclusive and the include list is having a bigger priority.
     *
     * @param propertyGroups list of properties to be filtered.
     * @param includeList    list of the included keys.
     * @param excludeList    list of the excluded keys.
     */
    void filterPropertyGroupProperties(List<PropertyGroup> propertyGroups, List<String> includeList, List<String> excludeList) {
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

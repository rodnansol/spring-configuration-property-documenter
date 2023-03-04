package org.rodnansol.core.generator.writer.postprocess;

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

    private final List<PropertyGroupPostProcessor> propertyGroupPostProcessors;

    public PropertyGroupFilterService(List<PropertyGroupPostProcessor> propertyGroupPostProcessors) {
        this.propertyGroupPostProcessors = propertyGroupPostProcessors;
    }

    public PropertyGroupFilterService() {
        this(List.of(
            new UnknownGroupRemovalPostProcessor(),
            new IncludeExcludeGroupPostProcessor(),
            new IncludeExcludePropertyPostProcessor(),
            new EmptyGroupRemovalPostProcessor(),
            new UnknownGroupRenamePostProcessor()
        ));
    }

    /**
     * Post process the incoming property groups with the different attributes.
     * <p>
     * Runs the list of the given post processors.
     *
     * @param command command that contains all the attributes that is needed for the process.
     */
    public void postProcessPropertyGroups(PostProcessPropertyGroupsCommand command) {
        try {
            LOGGER.debug("Running post processors with the following command:[{}] - List of post processors:[{}]", command, propertyGroupPostProcessors);
            propertyGroupPostProcessors.forEach(propertyGroupPostProcessor -> propertyGroupPostProcessor.postProcess(command));
        } catch (Exception e) {
            LOGGER.warn("Error during filtering the property groups and properties, no filtering logic will be applied, please check the logs.", e);
        }
    }


}

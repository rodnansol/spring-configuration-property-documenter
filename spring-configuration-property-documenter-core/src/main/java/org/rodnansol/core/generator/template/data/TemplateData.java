package org.rodnansol.core.generator.template.data;

import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.util.List;

/**
 * Interface representing the ancestor for all template data types.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public interface TemplateData {

    /**
     * Returns the template customization data.
     *
     * @since 0.4.0
     */
    TemplateCustomization getTemplateCustomization();

    /**
     * Sets the template customization data.
     *
     * @since 0.4.0
     */
    void setTemplateCustomization(TemplateCustomization templateCustomization);

    /**
     * Returns all properties associated with the template data.
     * <p>
     * It aggregates the properties from all available property groups.
     *
     * @return aggregated list of properties.
     * @since 0.6.0
     */
    List<Property> getAggregatedProperties();

}

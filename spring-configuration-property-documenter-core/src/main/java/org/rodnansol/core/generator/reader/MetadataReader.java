package org.rodnansol.core.generator.reader;

import org.rodnansol.core.generator.template.Property;
import org.rodnansol.core.generator.template.PropertyDeprecation;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata;
import org.springframework.boot.configurationprocessor.metadata.ItemDeprecation;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Reads the spring-configuration-metadata.json file.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class MetadataReader {

    public static final MetadataReader INSTANCE = new MetadataReader();

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataReader.class);

    private MetadataReader() {
    }

    /**
     * Returns the properties in a map where the key is the name of the properties key and the values is the associated properties.
     *
     * @param metadataStream stream containing the content of the <code>spring-configuration-metadata.json</code>.
     * @return groups and properties converted to a Map.
     * @since 0.1.0
     */
    public Map<String, List<Property>> readPropertiesAsMap(InputStream metadataStream) {
        Objects.requireNonNull(metadataStream, "metadataStream is NULL");
        try {
            ConfigurationMetadata configurationMetadata = new JsonMarshaller().read(metadataStream);
            Map<String, List<Property>> propertyMap = getPropertyMap(configurationMetadata);
            LOGGER.trace("Configuration metadata contains number of properties:[{}]", propertyMap.size());
            return propertyMap;
        } catch (Exception e) {
            throw new MetadataConversionException("Error during converting properties to Map", e);
        }
    }

    /**
     * Returns a list of {@link PropertyGroup} instances from the given input stream.
     * <p>
     * <b>NOTE:</b> The current implementation is a bit fuzzy, when the time comes we can come up with a more efficient solution, but right now this is the "contact" basically.
     *
     * @param metadataStream stream containing the content of the <code>spring-configuration-metadata.json</code>.
     * @return groups and properties converted to a List of {@link PropertyGroup}.
     * @since 0.1.0
     */
    public List<PropertyGroup> readPropertiesAsPropertyGroupList(InputStream metadataStream) {
        Objects.requireNonNull(metadataStream, "metadataStream is NULL");
        try {
            ConfigurationMetadata configurationMetadata = new JsonMarshaller().read(metadataStream);
            Map<String, List<Property>> propertyMap = getPropertyMap(configurationMetadata);
            Map<String, List<PropertyGroup>> propertyGroupsByType = getPropertyGroups(configurationMetadata);
            updateGroupsWithPropertiesAndAssociations(propertyMap, propertyGroupsByType);
            LOGGER.trace("Configuration metadata contains number of group:[{}] and properties:[{}]", propertyGroupsByType.size(), propertyMap.size());
            return flattenValues(propertyGroupsByType);
        } catch (Exception e) {
            throw new MetadataConversionException("Error during converting properties to list of ProperyGroups", e);
        }
    }

    private PropertyGroup setProperties(Map<String, List<Property>> propertyMap, PropertyGroup propertyGroup) {
        List<Property> properties = propertyMap.get(propertyGroup.getType());
        if (properties == null || properties.isEmpty()) {
            LOGGER.warn("Property group with name:[{}] is having no properties, please check if you provided the getter/setter methods. If your class is empty intentionally, please forget this warning here.", propertyGroup.getGroupName());
            return propertyGroup;
        }
        List<Property> collectedProperties = properties.stream()
            .map(property -> updateProperty(propertyGroup, property))
            .collect(Collectors.toList());
        propertyGroup.setProperties(collectedProperties);
        return propertyGroup;
    }

    private Property updateProperty(PropertyGroup propertyGroup, Property property) {
        property.setKey(property.getFqName().substring(propertyGroup.getGroupName().length() + 1));
        return property;
    }

    private List<PropertyGroup> flattenValues(Map<String, List<PropertyGroup>> propertyGroupsByType) {
        return propertyGroupsByType.values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void updateGroupsWithPropertiesAndAssociations(Map<String, List<Property>> propertyMap, Map<String, List<PropertyGroup>> propertyGroupsByType) {
        for (Map.Entry<String, List<PropertyGroup>> entry : propertyGroupsByType.entrySet()) {
            List<PropertyGroup> nestedProperties = updatePropertiesAndReturnNestedGroups(propertyMap, entry);
            for (PropertyGroup nestedProperty : nestedProperties) {
                List<PropertyGroup> parentList = propertyGroupsByType.get(nestedProperty.getSourceType());
                parentList.stream().filter(propertyGroup -> propertyGroup.getType().equals(nestedProperty.getSourceType())).findFirst().ifPresent(parent -> {
                    parent.addChildGroup(nestedProperty);
                    nestedProperty.setParentGroup(parent);
                });
            }
        }
    }

    private List<PropertyGroup> updatePropertiesAndReturnNestedGroups(Map<String, List<Property>> propertyMap, Map.Entry<String, List<PropertyGroup>> propertyEntry) {
        return propertyEntry.getValue()
            .stream()
            .map(propertyGroup -> setProperties(propertyMap, propertyGroup))
            .filter(PropertyGroup::isNested).collect(Collectors.toList());
    }

    private Map<String, List<PropertyGroup>> getPropertyGroups(ConfigurationMetadata configurationMetadata) {
        return configurationMetadata.getItems()
            .stream()
            .filter(itemMetadata -> itemMetadata.isOfItemType(ItemMetadata.ItemType.GROUP))
            .map(itemMetadata -> new PropertyGroup(itemMetadata.getName(), itemMetadata.getType(), itemMetadata.getSourceType()))
            .collect(Collectors.groupingBy(PropertyGroup::getSourceType, Collectors.toList()));
    }

    private Map<String, List<Property>> getPropertyMap(ConfigurationMetadata configurationMetadata) {
        return configurationMetadata.getItems()
            .stream()
            .filter(itemMetadata -> itemMetadata.isOfItemType(ItemMetadata.ItemType.PROPERTY))
            .collect(Collectors.groupingBy(ItemMetadata::getSourceType,
                Collectors.mapping(this::mapToProperty, Collectors.toList()))
            );
    }

    private Property mapToProperty(ItemMetadata itemMetadata) {
        Property property = new Property(itemMetadata.getName(), itemMetadata.getType());
        property.setDescription(itemMetadata.getDescription());
        if (itemMetadata.getDefaultValue() != null) {
            property.setDefaultValue(itemMetadata.getDefaultValue().toString());
        }
        ItemDeprecation deprecation = itemMetadata.getDeprecation();
        if (deprecation != null) {
            PropertyDeprecation propertyDeprecation = new PropertyDeprecation(deprecation.getReason(), deprecation.getReplacement());
            property.setPropertyDeprecation(propertyDeprecation);
        }
        return property;
    }

}

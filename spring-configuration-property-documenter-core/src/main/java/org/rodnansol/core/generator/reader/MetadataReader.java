package org.rodnansol.core.generator.reader;

import org.rodnansol.core.generator.template.Property;
import org.rodnansol.core.generator.template.PropertyDeprecation;
import org.rodnansol.core.generator.template.PropertyGroup;
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata;
import org.springframework.boot.configurationprocessor.metadata.ItemDeprecation;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller;

import java.io.File;
import java.io.FileInputStream;
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

    private MetadataReader() {
    }

    /**
     * @param metadataFile
     * @return
     */
    public Map<String, List<Property>> readPropertiesAsMap(File metadataFile) {
        Objects.requireNonNull(metadataFile, "metadataFile is NULL");
        try {
            ConfigurationMetadata configurationMetadata = new JsonMarshaller().read(new FileInputStream(metadataFile));
            return getPropertyMap(configurationMetadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param metadataStream
     * @return
     */
    public List<PropertyGroup> readPropertiesAsList(InputStream metadataStream) {
        Objects.requireNonNull(metadataStream, "metadataFile is NULL");
        try {
            ConfigurationMetadata configurationMetadata = new JsonMarshaller().read(metadataStream);
            Map<String, List<Property>> propertyMap = getPropertyMap(configurationMetadata);
            Map<String, List<PropertyGroup>> propertyGroupsByType = getPropertyGroups(configurationMetadata);
            updateGroupsWithPropertiesAndAssociations(propertyMap, propertyGroupsByType);
            return flattenValues(propertyGroupsByType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PropertyGroup setProperties(Map<String, List<Property>> propertyMap, PropertyGroup propertyGroup) {
        propertyGroup.setProperties(propertyMap.get(propertyGroup.getType()).stream()
            .map(property -> {
                property.setKey(property.getFqName().substring(propertyGroup.getGroupName().length() + 1));
                return property;
            })
            .collect(Collectors.toList()));
        return propertyGroup;
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

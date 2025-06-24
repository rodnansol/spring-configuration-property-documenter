package org.rodnansol.core.generator.reader;

import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyDeprecation;
import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.data.PropertyGroupConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata;
import org.springframework.boot.configurationprocessor.metadata.ItemDeprecation;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
     * @param metadataStream stream containing the content of the <code>spring-configuration-metadata.json</code> or an empty list if the incoming stream is empty.
     * @return groups and properties converted to a List of {@link PropertyGroup}.
     * @since 0.1.0
     */
    public List<PropertyGroup> readPropertiesAsPropertyGroupList(InputStream metadataStream) {
        Objects.requireNonNull(metadataStream, "metadataStream is NULL");
        try {
            if (metadataStream.available() == 0) {
                return List.of();
            }
            ConfigurationMetadata configurationMetadata = new JsonMarshaller().read(metadataStream);
            Map<String, List<Property>> propertyMap = getPropertyMap(configurationMetadata);
            Map<String, List<PropertyGroup>> propertyGroupsBySourceType = getPropertyGroups(configurationMetadata); // Renamed for clarity
            updateGroupsWithPropertiesAndAssociations(propertyMap, propertyGroupsBySourceType);
            LOGGER.trace("Configuration metadata contains number of group:[{}] and properties:[{}]", propertyGroupsBySourceType.size(), propertyMap.size());
            return flattenValues(propertyGroupsBySourceType);
        } catch (Exception e) {
            throw new MetadataConversionException("Error during converting properties to list of ProperyGroups", e);
        }
    }

    private Property updateProperty(PropertyGroup propertyGroup, Property property) {
        String groupName = propertyGroup.getGroupName();
        if (propertyGroup.isUnknownGroup()) {
            property.setKey(property.getFqName());
        } else {
            if(propertyGroup.getGroupName().isBlank()) {
                property.setKey(property.getFqName());
            } else {
                // Ensure property name actually starts with groupName + "." before substring
                if (property.getFqName().startsWith(groupName + ".")) {
                    property.setKey(property.getFqName().substring(groupName.length() + 1));
                } else {
                    // If not, it might be a property directly in a "group" that's actually a top-level property,
                    // or a mismatch. For safety, use fqName.
                    property.setKey(property.getFqName());
                }
            }
        }
        return property;
    }

    private List<PropertyGroup> flattenValues(Map<String, List<PropertyGroup>> propertyGroupsBySourceType) {
        return propertyGroupsBySourceType.values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void updateGroupsWithPropertiesAndAssociations(Map<String, List<Property>> propertyMap, Map<String, List<PropertyGroup>> propertyGroupsBySourceType) {
        for (Map.Entry<String, List<PropertyGroup>> entry : propertyGroupsBySourceType.entrySet()) {
            List<PropertyGroup> nestedProperties = updatePropertiesAndReturnNestedGroups(propertyMap, entry);
            for (PropertyGroup nestedProperty : nestedProperties) {
                List<PropertyGroup> parentList = propertyGroupsBySourceType.get(nestedProperty.getSourceType());
                if (parentList != null) { // Added null check
                    parentList.stream().filter(propertyGroup -> propertyGroup.getType().equals(nestedProperty.getSourceType())).findFirst().ifPresent(parent -> {
                        parent.addChildGroup(nestedProperty);
                        nestedProperty.setParentGroup(parent);
                    });
                }
            }
        }
    }

    private List<PropertyGroup> updatePropertiesAndReturnNestedGroups(Map<String, List<Property>> propertyMap, Map.Entry<String, List<PropertyGroup>> propertyEntry) {
        return propertyEntry.getValue()
            .stream()
            .map(propertyGroup -> setProperties(propertyMap, propertyGroup))
            .filter(PropertyGroup::isNested)
            .collect(Collectors.toList());
    }

    private PropertyGroup setProperties(Map<String, List<Property>> propertyMap, PropertyGroup propertyGroup) {
        // Key for propertyMap should be the sourceType that was used when populating propertyMap.
        // This is propertyGroup.getSourceType() because getPropertyGroups also uses getSourceTypeOrDefault for groups.
        List<Property> potentialProperties = propertyMap.get(propertyGroup.getSourceType());
        if (potentialProperties == null || potentialProperties.isEmpty()) {
            if (!propertyGroup.isUnknownGroup() && !propertyGroup.getGroupName().isEmpty()) { // Avoid warning for truly unknown or empty named groups
                LOGGER.warn("Property group with name:[{}] and sourceType:[{}] has no associated properties. " +
                            "Check if getter/setter methods are provided or if 'sourceType' in 'additional-spring-configuration-metadata.json' is correct. " +
                            "If the group is intentionally empty, please ignore this warning.",
                            propertyGroup.getGroupName(), propertyGroup.getSourceType());
            }
            propertyGroup.setProperties(List.of()); // Ensure it's an empty list
            return propertyGroup;
        }
        List<Property> collectedProperties = potentialProperties.stream()
            .filter(property -> propertyGroup.isUnknownGroup() || property.getFqName().startsWith(propertyGroup.getGroupName()))
            .map(property -> updateProperty(propertyGroup, property))
            .collect(Collectors.toList());
        propertyGroup.setProperties(collectedProperties);
        return propertyGroup;
    }

    private Map<String, List<PropertyGroup>> getPropertyGroups(ConfigurationMetadata configurationMetadata) {
        Map<String, List<PropertyGroup>> propertyGroupMap = configurationMetadata.getItems()
            .stream()
            .filter(itemMetadata -> itemMetadata.isOfItemType(ItemMetadata.ItemType.GROUP))
            .map(itemMetadata -> new PropertyGroup(itemMetadata.getName(), itemMetadata.getType(), getSourceTypeOrDefault(itemMetadata))) // Pass itemMetadata itself
            .collect(Collectors.groupingBy(PropertyGroup::getSourceType, Collectors.toList()));
        // Ensure the canonical "Unknown" group is always there and other groups that might also have an "UNKNOWN" sourceType are preserved.
        propertyGroupMap.computeIfAbsent(PropertyGroupConstants.UNKNOWN, k -> new ArrayList<>()).add(0, PropertyGroup.createUnknownGroup()); // Add to existing list, perhaps at the beginning
        return propertyGroupMap;
    }

    private Map<String, List<Property>> getPropertyMap(ConfigurationMetadata configurationMetadata) {
        List<ItemMetadata> groupItems = configurationMetadata.getItems().stream()
            .filter(item -> item.isOfItemType(ItemMetadata.ItemType.GROUP))
            .collect(Collectors.toList());

        Function<ItemMetadata, String> propertySourceTypeResolver = propertyItem -> {
            // Priority 1: Explicit sourceType on the property item itself.
            if (propertyItem.getSourceType() != null) {
                return propertyItem.getSourceType();
            }

            // Priority 2: Infer from group if property name is prefixed by a group name.
            String propertyName = propertyItem.getName();
            ItemMetadata bestMatchingGroup = null;
            int longestMatchLength = -1; // Use -1 to ensure any group name match is longer

            for (ItemMetadata groupItem : groupItems) {
                String groupName = groupItem.getName();
                if (groupName == null || groupName.isEmpty()) continue; // Skip groups with no name

                // Check if propertyName starts with "groupName."
                if (propertyName.startsWith(groupName + ".")) {
                    if (groupName.length() > longestMatchLength) {
                        // This group is a more specific prefix.
                        // The sourceType for the property will be the group's sourceType or type.
                        String groupSourceType = groupItem.getSourceType() != null ? groupItem.getSourceType() : groupItem.getType();
                        if (groupSourceType != null) { // We need a valid sourceType from the group
                           longestMatchLength = groupName.length();
                           bestMatchingGroup = groupItem;
                        }
                    }
                }
            }

            if (bestMatchingGroup != null) {
                 // Use the sourceType of the best matching group, or its type if sourceType is null.
                return bestMatchingGroup.getSourceType() != null ? bestMatchingGroup.getSourceType() : bestMatchingGroup.getType();
            }

            // Priority 3: Fallback to UNKNOWN if no other sourceType can be determined.
            return PropertyGroupConstants.UNKNOWN;
        };

        return configurationMetadata.getItems()
            .stream()
            .filter(itemMetadata -> itemMetadata.isOfItemType(ItemMetadata.ItemType.PROPERTY))
            .collect(Collectors.groupingBy(propertySourceTypeResolver,
                Collectors.mapping(this::mapToProperty, Collectors.toList()))
            );
    }

    // getSourceTypeOrDefault is now primarily for groups.
    // A group's sourceType is what it declares. If it declares none, then its 'type' is used. If that's also none, it's UNKNOWN.
    private String getSourceTypeOrDefault(ItemMetadata groupItem) {
        return Optional.ofNullable(groupItem.getSourceType())
            .orElse(Optional.ofNullable(groupItem.getType())
            .orElse(PropertyGroupConstants.UNKNOWN));
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

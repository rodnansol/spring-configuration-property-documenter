package org.rodnansol.core.generator.reader;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.Issue;
import org.rodnansol.core.generator.template.data.Property;
import org.rodnansol.core.generator.template.data.PropertyDeprecation;
import org.rodnansol.core.generator.template.data.PropertyGroup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MetadataReaderTest {

    protected static final String TEST_RESOURCES_DIRECTORY = "src/test/resources/";

    private final MetadataReader underTest = MetadataReader.INSTANCE;

    static Stream<Arguments> validMetadataFileTestCasesWithPropertyGroups() {
        return Stream.of(
                arguments(named("Should read file and return empty property groups list when the groups and properties are empty", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty-groups-and-properties.json",
                        List::of))),
                arguments(named("Should read file and return non-empty property groups list when the groups are empty but the properties are not and properties are not having associated groups, in this case a new 'Unknown group' group should appear", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty-sourceType.json",
                        () -> {
                            PropertyGroup expectedMyProperties = new PropertyGroup("Unknown group", "Unknown", "Unknown");
                            expectedMyProperties.setUnknownGroup(true);
                            expectedMyProperties.addProperty(new Property("myproduct.features.foobar.enabled", "java.lang.Boolean", "myproduct.features.foobar.enabled", "Enable the foobar feature", "true", null));
                            return List.of(expectedMyProperties);
                        }))),
                arguments(named("Should read file and return empty property groups list when the groups and properties are empty", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty-groups-and-properties.json",
                        List::of))),
                arguments(named("Should read file and return non-empty property groups list when one group is given but no associated properties are present", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-one-group-empty-properties.json",
                        () -> {
                            PropertyGroup expectedMyProperties = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
                            return List.of(expectedMyProperties);
                        }))),
                arguments(named("Should read file and return non-empty property groups list when one group is present with one property", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-one-group-one-property.json",
                        () -> {
                            PropertyGroup expectedMyProperties = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
                            expectedMyProperties.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));
                            return List.of(expectedMyProperties);
                        }))),
                arguments(named("Should read file and return non-empty property groups list when the groups are having nested associations", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-with-nested-property.json",
                        () -> {
                            PropertyGroup expectedMyProperties = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
                            expectedMyProperties.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));

                            PropertyGroup expectedFirstLevelNestedProperty = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
                            expectedFirstLevelNestedProperty.setNested(true);
                            expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
                            expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
                            expectedFirstLevelNestedProperty.setParentGroup(expectedMyProperties);

                            PropertyGroup firstLevelNestedPropertySecondLevelNestedClass = new PropertyGroup("this.is.my.first-level-nested-property.second-level-nested-class", "com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", "com.example.springpropertysources.FirstLevelNestedProperty");
                            firstLevelNestedPropertySecondLevelNestedClass.setNested(true);
                            firstLevelNestedPropertySecondLevelNestedClass.addProperty(new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", "second-level-value", "Custom nested", null, null));

                            PropertyGroup expectedNestedProperties = new PropertyGroup("this.is.my.nested", "com.example.springpropertysources.TopLevelClassNestedProperty", "com.example.springpropertysources.TopLevelClassNestedProperty");
                            expectedNestedProperties.addProperty(new Property("this.is.my.nested.nested-value", "java.lang.String", "nested-value", "Nested value.", null, null));

                            return List.of(expectedMyProperties, expectedFirstLevelNestedProperty, PropertyGroup.createUnknownGroup(), firstLevelNestedPropertySecondLevelNestedClass, expectedNestedProperties);
                        }))),
                arguments(named("Should read file and return non-empty property groups list when the groups and properties are in complex relation", new ReadPropertiesAsPropertyGroupListTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-complex.json",
                        () -> {
                            PropertyGroup expectedYourProperties = new PropertyGroup("this.is.your", "com.example.springpropertysources.YourProperties", "com.example.springpropertysources.YourProperties");
                            expectedYourProperties.addProperty(new Property("this.is.your.property", "java.lang.String", "property", "This is YOUR property.", null, null));

                            PropertyGroup expectedMyProperties = new PropertyGroup("this.is.my", "com.example.springpropertysources.MyProperties", "com.example.springpropertysources.MyProperties");
                            expectedMyProperties.addProperty(new Property("this.is.my.another-variable", "java.lang.String", "another-variable", null, "with default value", new PropertyDeprecation(null, null)));
                            expectedMyProperties.addProperty(new Property("this.is.my.date", "java.time.LocalDate", "date", null, null, null));
                            expectedMyProperties.addProperty(new Property("this.is.my.date-time", "java.time.LocalDateTime", "date-time", null, null, null));
                            expectedMyProperties.addProperty(new Property("this.is.my.duration", "java.time.Duration", "duration", "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")));
                            expectedMyProperties.addProperty(new Property("this.is.my.instant", "java.time.Instant", "instant", null, "123", null));
                            expectedMyProperties.addProperty(new Property("this.is.my.variable", "java.lang.String", "variable", "This is my variable.", null, null));

                            PropertyGroup expectedFirstLevelNestedProperty = new PropertyGroup("this.is.my.first-level-nested-property", "com.example.springpropertysources.FirstLevelNestedProperty", "com.example.springpropertysources.MyProperties");
                            expectedFirstLevelNestedProperty.setNested(true);
                            expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", "desc", "Description of this thing.", "123", null));
                            expectedFirstLevelNestedProperty.addProperty(new Property("this.is.my.first-level-nested-property.name", "java.lang.String", "name", "Name of the custom property.", "ABC", null));
                            expectedFirstLevelNestedProperty.setParentGroup(expectedMyProperties);

                            PropertyGroup firstLevelNestedPropertySecondLevelNestedClass = new PropertyGroup("this.is.my.first-level-nested-property.second-level-nested-class", "com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", "com.example.springpropertysources.FirstLevelNestedProperty");
                            firstLevelNestedPropertySecondLevelNestedClass.setNested(true);
                            firstLevelNestedPropertySecondLevelNestedClass.addProperty(new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", "second-level-value", "Custom nested", null, null));

                            PropertyGroup expectedNestedProperties = new PropertyGroup("this.is.my.nested", "com.example.springpropertysources.TopLevelClassNestedProperty", "com.example.springpropertysources.TopLevelClassNestedProperty");
                            expectedNestedProperties.addProperty(new Property("this.is.my.nested.nested-value", "java.lang.String", "nested-value", "Nested value.", null, null));

                            return List.of(expectedYourProperties, expectedMyProperties, expectedFirstLevelNestedProperty, PropertyGroup.createUnknownGroup(), firstLevelNestedPropertySecondLevelNestedClass, expectedNestedProperties);
                        })))
        );
    }

    static Stream<Arguments> validMetadataFileTestCasesWithMaps() {
        return Stream.of(
                arguments(named("Should read file and return empty map when the groups and properties are empty", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty-groups-and-properties.json",
                        Map::of))),
                arguments(named("Should read file and return empty map when the groups and properties are empty", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty-groups-and-properties.json",
                        Map::of))),
                arguments(named("Should read file and return empty map when one group is given but no associated properties are present", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-one-group-empty-properties.json",
                        Map::of))),
                arguments(named("Should read file and return non-empty map when one group is present with one property", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-one-group-one-property.json",
                        () -> {
                            return Map.ofEntries(
                                    entry("com.example.springpropertysources.MyProperties", List.of(
                                            new Property("this.is.my.variable", "java.lang.String", null, "This is my variable.", null, null)
                                    ))
                            );
                        }))),
                arguments(named("Should read file and return non-empty map when the groups are having nested associations", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-with-nested-property.json",
                        () -> {
                            return Map.ofEntries(
                                    entry("com.example.springpropertysources.MyProperties", List.of(
                                            new Property("this.is.my.variable", "java.lang.String", null, "This is my variable.", null, null)
                                    )),
                                    entry("com.example.springpropertysources.FirstLevelNestedProperty", List.of(
                                            new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", null, "Description of this thing.", "123", null),
                                            new Property("this.is.my.first-level-nested-property.name", "java.lang.String", null, "Name of the custom property.", "ABC", null)
                                    )),
                                    entry("com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", List.of(
                                            new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", null, "Custom nested", null, null)
                                    )),
                                    entry("com.example.springpropertysources.TopLevelClassNestedProperty", List.of(
                                            new Property("this.is.my.nested.nested-value", "java.lang.String", null, "Nested value.", null, null)
                                    ))
                            );

                        }))),
                arguments(named("Should read file and return non-empty map when the groups and properties are in complex relation", new ReadPropertiesAsMapTestCase(TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-complex.json",
                        () -> {
                            return Map.ofEntries(
                                    entry("com.example.springpropertysources.YourProperties", List.of(new Property("this.is.your.property", "java.lang.String", null, "This is YOUR property.", null, null))),
                                    entry("com.example.springpropertysources.MyProperties", List.of(
                                            new Property("this.is.my.another-variable", "java.lang.String", null, null, "with default value", new PropertyDeprecation(null, null)),
                                            new Property("this.is.my.date", "java.time.LocalDate", null, null, null, null),
                                            new Property("this.is.my.date-time", "java.time.LocalDateTime", null, null, null, null),
                                            new Property("this.is.my.duration", "java.time.Duration", null, "A duration.", "2d", new PropertyDeprecation("Because it is deprecated", "instant")),
                                            new Property("this.is.my.instant", "java.time.Instant", null, null, "123", null),
                                            new Property("this.is.my.variable", "java.lang.String", null, "This is my variable.", null, null)
                                    )),
                                    entry("com.example.springpropertysources.FirstLevelNestedProperty", List.of(
                                            new Property("this.is.my.first-level-nested-property.desc", "java.lang.String", null, "Description of this thing.", "123", null),
                                            new Property("this.is.my.first-level-nested-property.name", "java.lang.String", null, "Name of the custom property.", "ABC", null)
                                    )),
                                    entry("com.example.springpropertysources.FirstLevelNestedProperty$SecondLevelNestedClass", List.of(
                                            new Property("this.is.my.first-level-nested-property.second-level-nested-class.second-level-value", "java.lang.String", null, "Custom nested", null, null)
                                    )),
                                    entry("com.example.springpropertysources.TopLevelClassNestedProperty", List.of(
                                            new Property("this.is.my.nested.nested-value", "java.lang.String", null, "Nested value.", null, null)
                                    ))
                            );
                        })))
        );
    }

    @ParameterizedTest
    @MethodSource("validMetadataFileTestCasesWithPropertyGroups")
    void readPropertiesAsPropertyGroupList_shouldReturnListOfPropertyGroup_whenInputIsGiven(ReadPropertiesAsPropertyGroupListTestCase readPropertiesAsPropertyGroupListTestCase) throws FileNotFoundException {
        // Given

        // When
        List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream(readPropertiesAsPropertyGroupListTestCase.metadataFileLocation));

        // Then
        assertThat(propertyGroups)
                .containsAll(readPropertiesAsPropertyGroupListTestCase.expectedPropertyGroupsSupplier.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-invalid.json"})
    void readPropertiesAsPropertyGroupList_shouldThrowMetadataConversionException_whenAnErrorOccursDuringConversion(String metadataFileLocation) throws FileNotFoundException {
        // Given

        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.readPropertiesAsPropertyGroupList(new FileInputStream(metadataFileLocation)))
                .isInstanceOf(MetadataConversionException.class);
        // Then

    }

    @ParameterizedTest
    @ValueSource(strings = {
        TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty.json"})
    void readPropertiesAsPropertyGroupList_shouldReturnEmptyList_whenInputIsEmpty(String metadataFileLocation) throws FileNotFoundException {
        // Given

        // When
        List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream(metadataFileLocation));

        // Then
        assertThat(propertyGroups).isEmpty();

    }

    @ParameterizedTest
    @MethodSource("validMetadataFileTestCasesWithMaps")
    void readPropertiesAsMap_shouldReturnListOfPropertyGroup_whenInputFileContainsProperties(ReadPropertiesAsMapTestCase testCase) throws FileNotFoundException {
        // Given

        // When
        Map<String, List<Property>> resultMap = underTest.readPropertiesAsMap(new FileInputStream(testCase.metadataFileLocation));

        // Then
        assertThat(resultMap)
                .containsAllEntriesOf(testCase.expectedPropertyMapSupplier.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-empty.json",
            TEST_RESOURCES_DIRECTORY + "spring-configuration-metadata-invalid.json"})
    void readPropertiesAsMap_shouldThrowMetadataConversionException_whenAnErrorOccursDuringConversion(String metadataFileLocation) throws FileNotFoundException {
        // Given

        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.readPropertiesAsMap(new FileInputStream(metadataFileLocation)))
                .isInstanceOf(MetadataConversionException.class);
        // Then

    }

    /**
     * RegressionTests class.
     */
    @Nested
    class RegressionTests {

        @Test
        @Issue("#71")
        @DisplayName("Should return the same property only once per property group when input is having the same input type multiple times defined")
        void readPropertiesAsPropertyGroupList_whenSingleTypeIsBeingReusedMultipleTimes() throws FileNotFoundException {
            // Given
            String fileName = TEST_RESOURCES_DIRECTORY + "regression/spring-configuration-metadata-multiple-nested-with-same-type.json";

            // When
            List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream(fileName));

            // Then
            PropertyGroup topLevelGroup = new PropertyGroup("my.app.parent.my-app-configuration", "com.playground.springplayground.MyApplicationConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            topLevelGroup.addProperty(new Property("my.app.parent.my-app-configuration.foo", "java.lang.String", "foo", null, null, null));

            PropertyGroup nestedFirst = new PropertyGroup("my.app.parent.my-app-configuration.abc", "com.playground.springplayground.MyApplicationConfiguration$MyNestedChildConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            nestedFirst.setNested(true);
            nestedFirst.setParentGroup(topLevelGroup);
            nestedFirst.addProperty(new Property("my.app.parent.my-app-configuration.abc.bar", "java.lang.String", "bar", null, null, null));

            PropertyGroup nestedSecond = new PropertyGroup("my.app.parent.my-app-configuration.efg", "com.playground.springplayground.MyApplicationConfiguration$MyNestedChildConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            nestedSecond.setNested(true);
            nestedSecond.setParentGroup(topLevelGroup);
            nestedSecond.addProperty(new Property("my.app.parent.my-app-configuration.efg.bar", "java.lang.String", "bar", null, null, null));

            PropertyGroup nestedThird = new PropertyGroup("my.app.parent.my-app-configuration.xyz", "com.playground.springplayground.MyApplicationConfiguration$MyNestedChildConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            nestedThird.setNested(true);
            nestedThird.setParentGroup(topLevelGroup);
            nestedThird.addProperty(new Property("my.app.parent.my-app-configuration.xyz.bar", "java.lang.String", "bar", null, null, null));


            List<PropertyGroup> expectedYourProperties1 = List.of(PropertyGroup.createUnknownGroup(), topLevelGroup, nestedFirst, nestedSecond, nestedThird);
            assertThat(propertyGroups)
                    .containsAll(expectedYourProperties1);
        }

        @Test
        @Issue("#72")
        @DisplayName("Should return properties with correct 'key' when type is used multiple times with different prefixes")
        void readPropertiesAsPropertyGroupList() throws FileNotFoundException {
            // Given
            String fileName = TEST_RESOURCES_DIRECTORY + "regression/spring-configuration-metadata-with-variable-field-name.json";

            // When
            List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream(fileName));

            // Then
            PropertyGroup topLevelGroup = new PropertyGroup("my.app.parent.my-app-configuration", "com.playground.springplayground.MyApplicationConfiguration", "com.playground.springplayground.MyApplicationConfiguration");

            PropertyGroup nestedFirst = new PropertyGroup("my.app.parent.my-app-configuration.abcd", "com.playground.springplayground.MyApplicationConfiguration$MyNestedChildConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            nestedFirst.setNested(true);
            nestedFirst.setParentGroup(topLevelGroup);
            nestedFirst.addProperty(new Property("my.app.parent.my-app-configuration.abcd.ab", "java.lang.String", "ab", null, null, null));

            PropertyGroup nestedSecond = new PropertyGroup("my.app.parent.my-app-configuration.another-child-config", "com.playground.springplayground.MyApplicationConfiguration$MyNestedChildConfiguration", "com.playground.springplayground.MyApplicationConfiguration");
            nestedSecond.setNested(true);
            nestedSecond.setParentGroup(topLevelGroup);
            nestedSecond.addProperty(new Property("my.app.parent.my-app-configuration.another-child-config.ab", "java.lang.String", "ab", null, null, null));

            List<PropertyGroup> expectedYourProperties1 = List.of(PropertyGroup.createUnknownGroup(), topLevelGroup, nestedFirst, nestedSecond);
            assertThat(propertyGroups)
                    .containsAll(expectedYourProperties1);
        }

        @Test
        @Issue("#79")
        @DisplayName("Should return correct 'key' when property group is having empty name")
        void readPropertiesAsPropertyGroupList_whenProperyParentIsNotHavingName() throws FileNotFoundException {
            // Given
            String fileName = TEST_RESOURCES_DIRECTORY + "regression/spring-configuration-metadata-with-no-prefix-at-parent-level.json";

            // When
            List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(new FileInputStream(fileName));

            // Then
            PropertyGroup topLevelGroup = new PropertyGroup("", "com.example.springconfigpropertyreproducer.MyConfigurationProperties", "com.example.springconfigpropertyreproducer.MyConfigurationProperties");
            topLevelGroup.addProperty(new Property("my-property", "java.lang.String", "my-property", "Generic property", "propertyValue", null));


            List<PropertyGroup> expectedYourProperties1 = List.of(PropertyGroup.createUnknownGroup(), topLevelGroup);
            assertThat(propertyGroups)
                .containsAll(expectedYourProperties1);
        }

        @Test
        @Issue("https://github.com/rodnansol/spring-configuration-property-documenter/issues/112")
        @DisplayName("Should correctly process additional metadata with custom group and property without explicit sourceType for property")
        void readPropertiesAsPropertyGroupList_shouldHandleAdditionalMetadataCorrectly_whenCustomGroupAndPropertyAreDefined() {
            // Given
            String jsonInput = "{\n" +
                "    \"groups\": [\n" +
                "        {\n" +
                "            \"name\": \"audit.javers\",\n" +
                "            \"type\": \"audit.JaversProperties\",\n" +
                "            \"sourceType\": \"audit.JaversProperties\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"properties\": [\n" +
                "        {\n" +
                "            \"name\": \"audit.javers.enabled\",\n" +
                "            \"type\": \"java.lang.Boolean\",\n" +
                "            \"description\": \"Whether Javers audit logging is enabled.\",\n" +
                "            \"defaultValue\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"hints\": []\n" +
                "}";
            InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes(StandardCharsets.UTF_8));

            // When
            List<PropertyGroup> propertyGroups = underTest.readPropertiesAsPropertyGroupList(inputStream);

            // Then
            assertThat(propertyGroups).hasSizeGreaterThanOrEqualTo(1); // Expect at least the audit.javers group (and potentially the default 'Unknown group')

            Optional<PropertyGroup> auditJaversGroupOpt = propertyGroups.stream()
                .filter(pg -> "audit.javers".equals(pg.getGroupName()))
                .findFirst();

            assertThat(auditJaversGroupOpt).isPresent();
            PropertyGroup auditJaversGroup = auditJaversGroupOpt.get();

            assertThat(auditJaversGroup.getType()).isEqualTo("audit.JaversProperties");
            assertThat(auditJaversGroup.getSourceType()).isEqualTo("audit.JaversProperties");
            assertThat(auditJaversGroup.getProperties()).hasSize(1);

            Property enabledProperty = auditJaversGroup.getProperties().get(0);
            assertThat(enabledProperty.getFqName()).isEqualTo("audit.javers.enabled");
            assertThat(enabledProperty.getKey()).isEqualTo("enabled"); // After processing by updateProperty
            assertThat(enabledProperty.getType()).isEqualTo("java.lang.Boolean");
            assertThat(enabledProperty.getDefaultValue()).isEqualTo("true");
            assertThat(enabledProperty.getDescription()).isEqualTo("Whether Javers audit logging is enabled.");

            // Ensure the property is not in the "Unknown group" if that group exists and is empty or doesn't contain this property
            // More robust check: assert that no other group except auditJaversGroup contains this property by FQ name.
            propertyGroups.stream()
                .filter(pg -> !"audit.javers".equals(pg.getGroupName()))
                .forEach(otherGroup -> {
                    boolean propertyInOtherGroup = otherGroup.getProperties().stream()
                        .anyMatch(p -> "audit.javers.enabled".equals(p.getFqName()));
                    assertThat(propertyInOtherGroup)
                        .as("Property audit.javers.enabled should not be in group: " + otherGroup.getGroupName())
                        .isFalse();
                });
        }
    }

    static class ReadPropertiesAsPropertyGroupListTestCase {

        public final String metadataFileLocation;
        public final Supplier<List<PropertyGroup>> expectedPropertyGroupsSupplier;

        public ReadPropertiesAsPropertyGroupListTestCase(String metadataFileLocation, Supplier<List<PropertyGroup>> expectedPropertyGroupsSupplier) {
            this.metadataFileLocation = metadataFileLocation;
            this.expectedPropertyGroupsSupplier = expectedPropertyGroupsSupplier;
        }

    }

    static class ReadPropertiesAsMapTestCase {

        public final String metadataFileLocation;
        public final Supplier<Map<String, List<Property>>> expectedPropertyMapSupplier;

        public ReadPropertiesAsMapTestCase(String metadataFileLocation, Supplier<Map<String, List<Property>>> expectedPropertyMapSupplier) {
            this.metadataFileLocation = metadataFileLocation;
            this.expectedPropertyMapSupplier = expectedPropertyMapSupplier;
        }

    }

}

# single-module-example
## Table of Contents
* [**Unknown group** - `Unknown`](#Unknown group)
* [**this.is.your** - `org.rodnansol.YourProperties`](#this.is.your)
* [**this.is.a.bean.configuration** - `org.rodnansol.PropertiesForConfiguration`](#this.is.a.bean.configuration)
* [**this.is.my.nested** - `org.rodnansol.TopLevelClassNestedProperty`](#this.is.my.nested)
* [**this.is.my.first-level-nested-property.second-level-nested-class** - `org.rodnansol.FirstLevelNestedProperty$SecondLevelNestedClass`](#this.is.my.first-level-nested-property.second-level-nested-class)
* [**this.is.my** - `org.rodnansol.MyProperties`](#this.is.my)
* [**this.is.my.first-level-nested-property** - `org.rodnansol.FirstLevelNestedProperty`](#this.is.my.first-level-nested-property)

### Unknown group
**Class:** `Unknown`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
### this.is.your
**Class:** `org.rodnansol.YourProperties`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| property| java.lang.String| This is YOUR property.| | | `THIS_IS_YOUR_PROPERTY`|
### this.is.a.bean.configuration
**Class:** `org.rodnansol.PropertiesForConfiguration`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| configuration-value| java.lang.String| A field inside a class that is not property by default by within a Configuration class.| | | `THIS_IS_A_BEAN_CONFIGURATION_CONFIGURATIONVALUE`|
### this.is.my.nested
**Class:** `org.rodnansol.TopLevelClassNestedProperty`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| nested-value| java.lang.String| Nested value.| | | `THIS_IS_MY_NESTED_NESTEDVALUE`|
### this.is.my.first-level-nested-property.second-level-nested-class
**Class:** `org.rodnansol.FirstLevelNestedProperty$SecondLevelNestedClass`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| second-level-value| java.lang.String| Custom nested| | | `THIS_IS_MY_FIRSTLEVELNESTEDPROPERTY_SECONDLEVELNESTEDCLASS_SECONDLEVELVALUE`|
### this.is.my
**Class:** `org.rodnansol.MyProperties`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| another-variable| java.lang.String| | with default value| Reason: null, use for replacement: null| `THIS_IS_MY_ANOTHERVARIABLE`|
| date| java.time.LocalDate| | | | `THIS_IS_MY_DATE`|
| date-time| java.time.LocalDateTime| | | | `THIS_IS_MY_DATETIME`|
| duration| java.time.Duration| A duration.| 2d| Reason: Because it is deprecated, use for replacement: instant| `THIS_IS_MY_DURATION`|
| instant| java.time.Instant| | 123| | `THIS_IS_MY_INSTANT`|
| variable| java.lang.String| This is my variable.| | | `THIS_IS_MY_VARIABLE`|
### this.is.my.first-level-nested-property
**Class:** `org.rodnansol.FirstLevelNestedProperty`

|Key|Type|Description|Default value|Deprecation|Environment variable |
|---|----|-----------|-------------|-----------|----------------------|
| desc| java.lang.String| Description of this thing.| 123| | `THIS_IS_MY_FIRSTLEVELNESTEDPROPERTY_DESC`|
| name| java.lang.String| Name of the custom property.| ABC| | `THIS_IS_MY_FIRSTLEVELNESTEDPROPERTY_NAME`|




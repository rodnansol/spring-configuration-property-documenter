package org.rodnansol.samples.gradle;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The properties are about my favourite things.
 * <p>
 * DOCS for my props.
 *
 * @author nandorholozsnyak
 */
@Component
@ConfigurationProperties(prefix = "this.is.my")
class MyProperties {

    /**
     * This is my variable.
     */
    private String variable;

    @NestedConfigurationProperty
    private FirstLevelNestedProperty firstLevelNestedProperty;

    @Deprecated(since = "Since you are a pilot")
    private String anotherVariable = "with default value";

    /**
     * A duration.
     */
    private Duration duration = Duration.ofDays(2);

    private Instant instant = Instant.ofEpochSecond(123);

    private LocalDate date = LocalDate.of(1995, 10, 20);

    private LocalDateTime dateTime = LocalDateTime.of(1995, 10, 20, 0, 1, 2, 3);

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getAnotherVariable() {
        return anotherVariable;
    }

    public void setAnotherVariable(String anotherVariable) {
        this.anotherVariable = anotherVariable;
    }


    @DeprecatedConfigurationProperty(reason = "Because it is deprecated", replacement = "instant")
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


    public FirstLevelNestedProperty getFirstLevelNestedProperty() {
        return firstLevelNestedProperty;
    }

    public void setFirstLevelNestedProperty(FirstLevelNestedProperty firstLevelNestedProperty) {
        this.firstLevelNestedProperty = firstLevelNestedProperty;
    }
}

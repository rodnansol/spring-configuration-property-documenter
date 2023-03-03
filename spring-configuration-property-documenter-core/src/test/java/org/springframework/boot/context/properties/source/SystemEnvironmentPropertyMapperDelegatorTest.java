package org.springframework.boot.context.properties.source;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SystemEnvironmentPropertyMapperDelegatorTest {

    @ParameterizedTest
    @CsvSource({
        "server.port,SERVER_PORT", // typical case
        "host[0].name,HOST_0_NAME", // indexed property
        "spring.main.banner-mode,SPRING_MAIN_BANNERMODE", // '-' in property name
    })
    void convertToEnvVariable(String input, String expected) {
        assertThat(SystemEnvironmentPropertyMapperDelegator.convertToEnvVariable(input))
            .isEqualTo(expected);
    }
}
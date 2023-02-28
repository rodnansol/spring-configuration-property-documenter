package org.rodnansol;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    @ConfigurationProperties("this.is.a.bean.configuration")
    public PropertiesForConfiguration propertiesForConfiguration() {
        return new PropertiesForConfiguration();
    }

}

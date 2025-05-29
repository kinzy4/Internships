package com.internshiptt.application.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;

@Configuration
public class EurekaConfig {
    @Bean
    public Jersey3TransportClientFactories jersey3TransportClientFactories() {
        return new Jersey3TransportClientFactories();
    }
}
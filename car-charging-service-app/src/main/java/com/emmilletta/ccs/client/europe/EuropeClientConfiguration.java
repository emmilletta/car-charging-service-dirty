package com.emmilletta.ccs.client.europe;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @author Alla Danko
 */
public class EuropeClientConfiguration {

    @Bean
    @Primary
    @Scope("prototype")
    public ErrorDecoder errorDecoder() {
        return new EuropeErrorDecoder();
    }
}

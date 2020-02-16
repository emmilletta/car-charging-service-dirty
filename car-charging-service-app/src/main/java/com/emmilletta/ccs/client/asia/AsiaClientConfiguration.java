package com.emmilletta.ccs.client.asia;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @author Alla Danko
 */
public class AsiaClientConfiguration {

    @Bean
    @Primary
    @Scope("prototype")
    public ErrorDecoder errorDecoder() {
        return new AsiaErrorDecoder();
    }
}

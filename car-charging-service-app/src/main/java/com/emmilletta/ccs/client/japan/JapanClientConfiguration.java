package com.emmilletta.ccs.client.japan;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;

/**
 * @author Alla Danko
 */
public class JapanClientConfiguration implements RequestInterceptor {

    @Autowired
    public JapanClientConfiguration() {
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ErrorDecoder errorDecoder() {
        return new JapanErrorDecoder();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Key " + "userAccessKey");
    }
}

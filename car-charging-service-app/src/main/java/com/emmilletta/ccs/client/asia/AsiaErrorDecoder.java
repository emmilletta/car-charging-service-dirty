package com.emmilletta.ccs.client.asia;

import com.emmilletta.ccs.client.asia.dto.AsiaBaseResponse;
import com.emmilletta.ccs.CException;
import com.emmilletta.ccs.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author Alla Danko
 */
@Slf4j
public class AsiaErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        CException exception = new CException();
        exception.setSourceSystem(SourceSystem.ASIA);
        exception.setStatus(HttpStatus.valueOf(response.status()));
        if (response.body() != null) {
            try {
                AsiaBaseResponse responseBody =
                        mapper.readValue(response.body().asInputStream(), AsiaBaseResponse.class);
                if (responseBody != null) {
                    exception.setErrorCode(responseBody.getCode());
                    exception.setErrorMessage(responseBody.getMessage());
                }
            } catch (IOException e) {
                log.error("Couldn't parse the body of response", e);
            }
        }
        return exception;
    }
}

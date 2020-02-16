package com.emmilletta.ccs.client.vis;

import com.emmilletta.ccs.client.vis.dto.VisBaseResponse;
import com.emmilletta.ccs.CException;
import com.emmilletta.ccs.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author Alla Danko
 */
@Slf4j
@NoArgsConstructor
public class VisErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        CException exception = new CException();
        exception.setSourceSystem(SourceSystem.VIS);
        exception.setStatus(HttpStatus.valueOf(response.status()));
        if (response.body() != null) {
            try {
                VisBaseResponse responseBody =
                        mapper.readValue(response.body().asInputStream(), VisBaseResponse.class);
                if (responseBody != null) {
                    exception.setErrorCode(responseBody.getResponseCode());
                    exception.setErrorMessage(responseBody.getResponseMessage());
                }
            } catch (IOException e) {
                log.error("Couldn't parse the body of response", e);
            }
        }
        return exception;
    }
}

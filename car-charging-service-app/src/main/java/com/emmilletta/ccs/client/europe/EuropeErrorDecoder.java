package com.emmilletta.ccs.client.europe;

import com.emmilletta.ccs.client.europe.dto.EuropeBaseResponse;
import com.emmilletta.ccs.CException;
import com.emmilletta.ccs.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Alla Danko
 */
@Slf4j
@NoArgsConstructor
@Component
public class EuropeErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        CException exception = new CException();
        exception.setSourceSystem(SourceSystem.EUROPE);
        exception.setStatus(HttpStatus.valueOf(response.status()));
        if (response.body() != null) {
            try {
                EuropeBaseResponse responseBody =
                        mapper.readValue(response.body().asInputStream(), EuropeBaseResponse.class);
                if (responseBody != null) {
                    exception.setErrorCode(responseBody.getErrorCode());
                    exception.setErrorMessage(responseBody.getErrorMessage());
                }
            } catch (IOException e) {
                log.error("Couldn't parse the body of response", e);
            }
        }
        return exception;
    }
}

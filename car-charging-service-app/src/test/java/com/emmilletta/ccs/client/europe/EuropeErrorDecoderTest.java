package com.emmilletta.ccs.client.europe;

import com.emmilletta.ccs.client.europe.dto.EuropeBaseResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingResponse;
import com.emmilletta.ccs.CException;
import com.emmilletta.ccs.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author Alla Danko
 */
@ExtendWith(MockitoExtension.class)
class EuropeErrorDecoderTest {

    private final SourceSystem SOURCE_SYSTEM = SourceSystem.EUROPE;
    private final HttpStatus HTTP_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    private EuropeErrorDecoder europeErrorDecoder = new EuropeErrorDecoder();

    @Test
    void decodeWithoutBodyTest() throws IOException {
        Exception result = europeErrorDecoder.decode(null, buildResponse(null, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(SOURCE_SYSTEM, ex.getSourceSystem());
        Assert.assertEquals(HTTP_STATUS, ex.getStatus());
    }

    @Test
    void decodeWithWrongBodyTest() throws IOException {
        EuropeStartChargingResponse response = EuropeStartChargingResponse.builder().build();
        Exception result = europeErrorDecoder.decode(null, buildResponse(response, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(SOURCE_SYSTEM, ex.getSourceSystem());
        Assert.assertEquals(HTTP_STATUS, ex.getStatus());
    }

    @Test
    void decodeSuccessfulTest() throws IOException {
        String errorMessage = "error_message";
        String errorCode = "error_code";
        EuropeBaseResponse response = EuropeBaseResponse.builder().errorCode(errorCode).errorMessage(errorMessage).build();
        Exception result = europeErrorDecoder.decode(null, buildResponse(response, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(errorCode, ex.getErrorCode());
        Assert.assertEquals(errorMessage, ex.getErrorMessage());
        Assert.assertEquals(SOURCE_SYSTEM, ex.getSourceSystem());
        Assert.assertEquals(HTTP_STATUS, ex.getStatus());
    }

    private Response buildResponse(Object body, int status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return Response.builder()
                .status(status)
                .body(body == null ? null : objectMapper.writeValueAsString(body).getBytes())
                .headers(new HashMap<>())
                .reason("")
                .request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
                        objectMapper.writeValueAsString(body).getBytes(), Charset.defaultCharset()))
                .build();
    }
}
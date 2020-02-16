package com.emmilletta.ccs.client.vis;

import com.emmilletta.ccs.client.vis.dto.VisAccessTokenResponse;
import com.emmilletta.ccs.client.vis.dto.VisBaseResponse;
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
class VisErrorDecoderTest {


    private final SourceSystem SOURCE_SYSTEM = SourceSystem.VIS;
    private final HttpStatus HTTP_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    private VisErrorDecoder visErrorDecoder = new VisErrorDecoder();

    @Test
    void decodeWithoutBodyTest() throws IOException {
        Exception result = visErrorDecoder.decode(null, buildResponse(null, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(SOURCE_SYSTEM, ex.getSourceSystem());
        Assert.assertEquals(HTTP_STATUS, ex.getStatus());
    }

    @Test
    void decodeWithWrongBodyTest() throws IOException {
        VisAccessTokenResponse response = VisAccessTokenResponse.builder().build();
        Exception result = visErrorDecoder.decode(null, buildResponse(response, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(SOURCE_SYSTEM, ex.getSourceSystem());
        Assert.assertEquals(HTTP_STATUS, ex.getStatus());
    }

    @Test
    void decodeSuccessfulTest() throws IOException {
        String error_message = "error_message";
        String error_code = "error_code";
        VisBaseResponse response = VisBaseResponse.builder().responseCode(error_code).responseMessage(error_message).build();
        Exception result = visErrorDecoder.decode(null, buildResponse(response, HTTP_STATUS.value()));
        Assert.assertTrue(result instanceof CException);
        CException ex = (CException)result;
        Assert.assertEquals(error_code, ex.getErrorCode());
        Assert.assertEquals(error_message, ex.getErrorMessage());
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
package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.SourceSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alla Danko
 */
@Slf4j
@Component
@RestControllerAdvice
public class Handle {

    @Autowired
    public Handle(){
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CcsResponse> processBadRequest(MethodArgumentNotValidException exception, WebRequest webRequest) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        CcsResponse response;
        List<String> fields = Arrays.asList(fieldError.getField().split("\\."));
        String field = fields.get(fields.size() - 1);
        String strMessage = String.format(CcsResponseStatus.MISSING_PARAMETER.getMessage(), field);
        response = CcsResponse.builder().message(strMessage).source(SourceSystem.CCS)
                .responseCode(CcsResponseStatus.MISSING_PARAMETER).build();        
        log.error("Bad request exception: {}. {}", exception.getMessage(), exception.getStackTrace());
        return ResponseEntity.status(response.getResponseCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CcsResponse> processException(Exception exception, WebRequest webRequest) {
        CcsResponse response = CcsResponse.builder().data(null)
                .message(CcsResponseStatus.INTERNAL_SERVER_ERROR.getMessage())
                .responseCode(CcsResponseStatus.INTERNAL_SERVER_ERROR).source(SourceSystem.CCS).build();
        log.error("General exception: {}. {}", exception.getMessage(), exception.getStackTrace());
        return ResponseEntity.status(response.getResponseCode().getHttpStatus()).body(response);
    }
}

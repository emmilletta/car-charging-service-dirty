package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.SourceSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Alla Danko
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CException extends RuntimeException {

    private HttpStatus status;
    private SourceSystem sourceSystem;
    private String errorCode;
    private String errorMessage;

}

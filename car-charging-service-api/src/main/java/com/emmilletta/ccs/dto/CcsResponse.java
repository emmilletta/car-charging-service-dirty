package com.emmilletta.ccs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alla Danko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CcsResponse<T> {

    private CcsResponseStatus responseCode;
    private String message;
    private SourceSystem source;
    private T data;
}

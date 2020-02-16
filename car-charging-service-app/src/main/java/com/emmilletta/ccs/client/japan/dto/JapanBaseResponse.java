package com.emmilletta.ccs.client.japan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class JapanBaseResponse<T> {

    @JsonProperty(value = "responseCode")
    private String responseCode;
    @JsonProperty(value = "responseMessage")
    private String responseMessage;
    @JsonProperty(value = "data")
    private T data;
}

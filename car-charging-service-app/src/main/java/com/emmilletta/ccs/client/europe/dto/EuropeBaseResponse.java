package com.emmilletta.ccs.client.europe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeBaseResponse {

    @JsonProperty(value = "error_code")
    private String errorCode;
    @JsonProperty(value = "error_message")
    private String errorMessage;
}

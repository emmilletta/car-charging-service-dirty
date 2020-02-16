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
public class JapanRegistrationResponse {

    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "userAccessKey")
    private String userAccessKey;
}

package com.emmilletta.ccs.client.europe.dto;

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
public class EuropeRegistrationRequest {

    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "vehicle")
    private String vehicle;
}

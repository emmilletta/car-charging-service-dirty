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
public class EuropeRegistrationResponse extends EuropeBaseResponse {

    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "status")
    private String status;
}

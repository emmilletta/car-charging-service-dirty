package com.emmilletta.ccs.client.asia.dto;

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
public class AsiaRegistrationResponse extends AsiaBaseResponse {

    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "providerUserId")
    private String providerUserId;
}

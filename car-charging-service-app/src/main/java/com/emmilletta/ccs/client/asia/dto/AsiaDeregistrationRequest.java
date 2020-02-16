package com.emmilletta.ccs.client.asia.dto;

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
public class AsiaDeregistrationRequest {

    @JsonProperty(value = "providerUserId")
    private String providerUserId;
    @JsonProperty(value = "vehicle")
    private String vehicle;
}

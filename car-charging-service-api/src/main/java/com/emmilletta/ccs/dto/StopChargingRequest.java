package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
public class StopChargingRequest extends CcsRequest {

    @NotBlank
    @JsonProperty(value = "sessionId", required = true)
    private String sessionId;
    @NotNull
    @JsonProperty(value = "provider", required = true)
    private ProviderEnum provider;

    @JsonCreator
    public StopChargingRequest(String userId, String vehicleId, String sessionId, ProviderEnum provider) {
        super(userId, vehicleId);
        this.sessionId = sessionId;
        this.provider = provider;
    }
}

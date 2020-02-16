package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
public class DeregistrationUserRequest extends UserIdRequest {

    @NotNull
    @JsonProperty(value = "provider", required = true)
    private ProviderEnum provider;
    @Valid
    @JsonProperty(value = "vehicles")
    private RegistrationVehicle vehicle;

    @JsonCreator
    public DeregistrationUserRequest(String userId, ProviderEnum provider, RegistrationVehicle vehicle) {
        super(userId);
        this.provider = provider;
        this.vehicle = vehicle;
    }
}

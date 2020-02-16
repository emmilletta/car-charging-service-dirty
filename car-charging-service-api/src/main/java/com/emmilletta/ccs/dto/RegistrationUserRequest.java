package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Alla Danko
 */
@Data
@Builder
public class RegistrationUserRequest {

    @Email
    @NotBlank
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotNull
    @JsonProperty(value = "provider", required = true)
    private ProviderEnum provider;

    @Valid
    @JsonProperty(value = "vehicle")
    private RegistrationVehicle vehicle;

    @JsonCreator
    public RegistrationUserRequest(String email, ProviderEnum provider, RegistrationVehicle vehicle) {
        this.email = email;
        this.provider = provider;
        this.vehicle = vehicle;
    }
}

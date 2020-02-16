package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
public class StartChargingRequest extends CcsRequest {

    @NotNull
    @JsonProperty(value = "provider", required = true)
    private ProviderEnum provider;
    @JsonProperty(value = "vendor")
    private VendorEnum vendor;

    @JsonCreator
    public StartChargingRequest(String userId, String vehicleId, ProviderEnum provider, VendorEnum vendor) {
        super(userId, vehicleId);
        this.provider = provider;
        this.vendor = vendor;
    }
}

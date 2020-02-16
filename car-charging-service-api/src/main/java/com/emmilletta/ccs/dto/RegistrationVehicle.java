package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Alla Danko
 */
@Data
@Builder
@NoArgsConstructor
public class RegistrationVehicle {

    @NotBlank
    @JsonProperty(value = "vehicleId", required = true)
    private String vehicleId;
    @JsonProperty(value = "vendor")
    private VendorEnum vendor;

    @JsonCreator
    public RegistrationVehicle(String vehicleId, VendorEnum vendor) {
        this.vehicleId = vehicleId;
        this.vendor = vendor;
    }
}

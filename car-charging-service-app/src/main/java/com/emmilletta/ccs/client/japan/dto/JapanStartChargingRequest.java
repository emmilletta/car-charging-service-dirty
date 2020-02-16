package com.emmilletta.ccs.client.japan.dto;

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
public class JapanStartChargingRequest {

    @JsonProperty(value = "vehicle_id")
    private String vehicleId;
}

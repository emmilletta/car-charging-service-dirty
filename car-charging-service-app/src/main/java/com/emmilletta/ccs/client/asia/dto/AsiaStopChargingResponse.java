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
public class AsiaStopChargingResponse extends AsiaBaseResponse {

    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "provider_user_id")
    private String providerUserId;
    @JsonProperty(value = "vehicle_id")
    private String vehicleId;
}

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
public class AsiaStopChargingRequest {

    @JsonProperty(value = "session_id")
    private String sessionId;
    @JsonProperty(value = "provider_user_id")
    private String providerUserId;
    @JsonProperty(value = "vehicle_id")
    private String vehicleId;
}

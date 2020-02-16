package com.emmilletta.ccs.client.europe.dto;

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
public class EuropeStopChargingRequest {

    @JsonProperty(value = "session_id")
    private String sessionId;
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "vehicle_id")
    private String vehicleId;
}

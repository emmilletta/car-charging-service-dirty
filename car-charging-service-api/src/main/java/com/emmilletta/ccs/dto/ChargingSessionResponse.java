package com.emmilletta.ccs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * @author Alla Danko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSessionResponse {

    private String sessionId;
    private String userId;
    private String vehicleId;
    private ProviderEnum provider;
    private SessionStatus status;
    private ZonedDateTime startTime;
    private ZonedDateTime stopTime;
}

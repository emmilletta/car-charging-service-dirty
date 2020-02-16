package com.emmilletta.ccs.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Alla Danko
 */
@Data
@Builder
public class StopChargingResponse {

    private String sessionId;
    private String userId;
    private ProviderEnum provider;
    private VendorEnum vendor;
    private String vehicleId;
}

package com.emmilletta.ccs.dto;

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
public class StartChargingResponse {

    private String sessionId;
    private String userId;
    private ProviderEnum provider;
    private VendorEnum vendor;
    private String vehicleId;
}

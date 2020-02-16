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
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsiaStartChargingResponse extends AsiaBaseResponse {

    @JsonProperty(value = "session_id")
    private String sessionId;
    @JsonProperty(value = "provider_user_id")
    private String providerUserId;
    @JsonProperty(value = "vin")
    private String vin;
}

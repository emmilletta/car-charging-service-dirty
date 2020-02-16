package com.emmilletta.ccs.client.vis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author Alla Danko
 */
@Data
@Builder
public class VisAccessTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "start_time")
    private ZonedDateTime startTime;
    @JsonProperty(value = "end_time")
    private ZonedDateTime endTime;
}

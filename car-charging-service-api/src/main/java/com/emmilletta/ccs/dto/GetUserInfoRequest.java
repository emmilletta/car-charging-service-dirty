package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
public class GetUserInfoRequest {

    @JsonProperty(value = "userId")
    private String userId;
    @JsonProperty(value = "status")
    private UserStatus status;
    @JsonProperty(value = "startDate")
    private LocalDate startDate;
    @JsonProperty(value = "stopDate")
    private LocalDate stopDate;

    @JsonCreator
    public GetUserInfoRequest(String userId, UserStatus status, LocalDate startDate, LocalDate stopDate) {
        this.userId = userId;
        this.status = status;
        this.startDate = startDate;
        this.stopDate = stopDate;
    }
}

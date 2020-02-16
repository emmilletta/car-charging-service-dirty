package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * @author Alla Danko
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class UserIdRequest {

    @NotBlank
    @JsonProperty(value = "userId", required = true)
    private String userId;
}

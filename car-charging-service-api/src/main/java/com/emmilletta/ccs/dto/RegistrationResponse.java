package com.emmilletta.ccs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alla Danko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

    private String userId;
    private String email;
    private ZonedDateTime registrationDate;
    private List<RegistrationVehicle> vehicles;
}

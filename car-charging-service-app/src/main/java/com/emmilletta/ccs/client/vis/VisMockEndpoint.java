package com.emmilletta.ccs.client.vis;

import com.emmilletta.ccs.client.vis.dto.VisAccessTokenResponse;
import com.emmilletta.ccs.client.vis.dto.VisBaseResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alla Danko
 */
@RestController
public class VisMockEndpoint implements Vis {

    private static final Integer RANDOM_LENGTH = 20;

    private static final String OK = "OK";
    private static final String VEHICLE_NOT_FOUND = "vehicle_not_found";
    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    @Override
    public ResponseEntity<VisBaseResponse<VisAccessTokenResponse>> getAccessToken(@RequestParam String vehicleId) {
        ResponseEntity responseEntity = validateRequest(vehicleId);
        if (responseEntity == null) {
            VisBaseResponse response = VisBaseResponse.builder().responseCode(OK).build();
            response.setData(VisAccessTokenResponse.builder()
                    .accessToken(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH)).build());
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    private ResponseEntity validateRequest(String vehicleId) {
        VisBaseResponse response = VisBaseResponse.builder().build();
        switch (vehicleId) {
            case VEHICLE_NOT_FOUND:
                response.setResponseCode(VisErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case INTERNAL_SERVER_ERROR:
                response.setResponseCode(VisErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return null;
    }
}

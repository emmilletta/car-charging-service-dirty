package com.emmilletta.ccs.client.japan;

import com.emmilletta.ccs.client.japan.dto.JapanBaseResponse;
import com.emmilletta.ccs.client.japan.dto.JapanDeregistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alla Danko
 */
@RestController
public class JapanMockEndpoint implements Japan {

    private static final Integer RANDOM_LENGTH = 16;

    private static final String OK = "OK";
    private static final String UNAUTHORIZED_ACCESS = "unauthorized_access";
    private static final String VEHICLE_NOT_FOUND = "vehicle_not_found";
    private static final String VEHICLE_NOT_CONNECTED = "vehicle_not_connected";
    private static final String WRONG_CAR_TYPE = "wrong_car_type";
    private static final String INVALID_SESSION_ID = "invalid_session_id";
    private static final String STATION_NOT_AVAILABLE = "station_not_available";
    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    @Override
    public ResponseEntity<JapanBaseResponse<JapanStartChargingResponse>> startCharging(
            @RequestBody JapanStartChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getVehicleId());
        if (responseEntity == null) {
            JapanBaseResponse response = JapanBaseResponse.builder().responseCode(OK).build();
            response.setData(JapanStartChargingResponse.builder()
                    .sessionId(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH)).build());
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<JapanBaseResponse<JapanStopChargingResponse>> stopCharging(
            @RequestBody JapanStopChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getSessionId());
        if (responseEntity == null) {
            JapanBaseResponse response = JapanBaseResponse.builder().responseCode(OK).build();
            if (INVALID_SESSION_ID.equals(request.getSessionId())){
                response.setResponseCode(JapanErrorCode.WRONG_SESSION_ID.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            response.setData(JapanStopChargingResponse.builder().status("ACTIVE").build());
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<JapanBaseResponse<JapanRegistrationResponse>> activateAccount(JapanRegistrationRequest request) {
        JapanBaseResponse response = JapanBaseResponse.builder().responseCode(OK).build();
        if (WRONG_CAR_TYPE.equals(request.getVehicle())) {
            response.setResponseCode(JapanErrorCode.NOT_SUPPORTED_CAR.getErrorCode());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        response.setData(JapanRegistrationResponse.builder()
                .email(request.getEmail())
                .userAccessKey(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH))
                .build());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<JapanBaseResponse> deactivateAccount(@RequestBody JapanDeregistrationRequest request) {
        JapanBaseResponse response = JapanBaseResponse.builder().responseCode(OK).build();
        if (request.getVehicleId() != null) {
            switch (request.getVehicleId()) {
                case WRONG_CAR_TYPE:
                    response.setResponseCode(JapanErrorCode.NOT_SUPPORTED_CAR.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                case VEHICLE_NOT_FOUND:
                    response.setResponseCode(JapanErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
        }
        return ResponseEntity.ok(response);
    }


    private ResponseEntity validateRequest(String vehicleId) {
        JapanBaseResponse response = JapanBaseResponse.builder().build();
        switch (vehicleId) {
            case STATION_NOT_AVAILABLE:
                response.setResponseCode(JapanErrorCode.STATION_BLOCKED.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case INTERNAL_SERVER_ERROR:
                response.setResponseCode(JapanErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            case VEHICLE_NOT_FOUND:
                response.setResponseCode(JapanErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case VEHICLE_NOT_CONNECTED:
                response.setResponseCode(JapanErrorCode.VEHICLE_NOT_CONNECTED.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        return null;
    }
}

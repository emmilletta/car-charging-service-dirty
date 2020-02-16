package com.emmilletta.ccs.client.asia;

import com.emmilletta.ccs.client.asia.dto.AsiaBaseResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaDeregistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alla Danko
 */
@RestController
public class AsiaMockEndpoint implements Asia {

    private static final Integer RANDOM_LENGTH = 16;

    private static final String WRONG_PROVIDER_USER_ID = "wrong_provider_user_id";
    private static final String VEHICLE_NOT_FOUND = "vehicle_not_found";
    private static final String VEHICLE_NOT_CONNECTED = "vehicle_not_connected";
    private static final String WRONG_CAR_TYPE = "wrong_car_type";
    private static final String INVALID_SESSION_ID = "invalid_session_id";
    private static final String STATION_NOT_AVAILABLE = "station_not_available";
    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    @Override
    public ResponseEntity<AsiaStartChargingResponse> startCharging(@RequestBody AsiaStartChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getProviderUserId(), request.getVehicleId());
        if (responseEntity == null) {
            AsiaStartChargingResponse response = AsiaStartChargingResponse.builder().build();
            response.setProviderUserId(request.getProviderUserId());
            response.setVin(request.getVehicleId());
            response.setSessionId(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH));
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<AsiaStopChargingResponse> stopCharging(@RequestBody AsiaStopChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getProviderUserId(), request.getVehicleId());
        if (responseEntity == null) {
            AsiaStopChargingResponse response = AsiaStopChargingResponse.builder().build();
            if (INVALID_SESSION_ID.equals(request.getSessionId())) {
                response.setCode(AsiaErrorCode.INVALID_SESSION_ID.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            response.setProviderUserId(request.getProviderUserId());
            response.setVehicleId(request.getVehicleId());
            response.setStatus("ACTIVE");
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<AsiaRegistrationResponse> registration(@RequestBody AsiaRegistrationRequest request) {
        AsiaRegistrationResponse response = AsiaRegistrationResponse.builder().build();
        if (WRONG_CAR_TYPE.equals(request.getVehicle())) {
            response.setCode(AsiaErrorCode.WRONG_CAR_TYPE.getErrorCode());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        response.setEmail(request.getEmail());
        response.setProviderUserId(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AsiaBaseResponse> deregistration(@RequestBody AsiaDeregistrationRequest request) {
        AsiaBaseResponse response = AsiaBaseResponse.builder().code("700100").build();
        if (request.getVehicle() != null) {
            switch (request.getVehicle()) {
                case WRONG_CAR_TYPE:
                    response.setCode(AsiaErrorCode.WRONG_CAR_TYPE.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                case VEHICLE_NOT_FOUND:
                    response.setCode(AsiaErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
        }
        if (WRONG_PROVIDER_USER_ID.equals(request.getProviderUserId())) {
            response.setCode(AsiaErrorCode.WRONG_PROVIDER_USER_ID.getErrorCode());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        return ResponseEntity.ok(response);
    }

    private ResponseEntity validateRequest(String providerUserId, String vehicleId) {
        AsiaBaseResponse response = AsiaBaseResponse.builder().build();
        switch (providerUserId) {
            case WRONG_PROVIDER_USER_ID:
                response.setCode(AsiaErrorCode.WRONG_PROVIDER_USER_ID.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case STATION_NOT_AVAILABLE:
                response.setCode(AsiaErrorCode.STATION_NOT_AVAILABLE.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case INTERNAL_SERVER_ERROR:
                response.setCode(AsiaErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            default:
                switch (vehicleId) {
                    case VEHICLE_NOT_FOUND:
                        response.setCode(AsiaErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                    case VEHICLE_NOT_CONNECTED:
                        response.setCode(AsiaErrorCode.VEHICLE_NOT_CONNECTED.getErrorCode());
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                }
        }
        return null;
    }
}

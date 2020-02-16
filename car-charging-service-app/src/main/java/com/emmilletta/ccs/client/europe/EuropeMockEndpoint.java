package com.emmilletta.ccs.client.europe;

import com.emmilletta.ccs.client.europe.dto.EuropeBaseResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeDeregistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alla Danko
 */
@RestController
public class EuropeMockEndpoint implements Europe {

    private static final Integer RANDOM_LENGTH = 16;

    private static final String WRONG_ACCESS_TOKEN = "wrong_access_token";
    private static final String VEHICLE_NOT_FOUND = "vehicle_not_found";
    private static final String VEHICLE_NOT_CONNECTED = "vehicle_not_connected";
    private static final String WRONG_CAR_TYPE = "wrong_car_type";
    private static final String INVALID_SESSION_ID = "invalid_session_id";
    private static final String STATION_NOT_AVAILABLE = "station_not_available";
    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    @Override
    public ResponseEntity<EuropeStartChargingResponse> startCharging(@RequestBody EuropeStartChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getVehicleId());
        if (responseEntity == null) {
            EuropeStartChargingResponse response = EuropeStartChargingResponse.builder().build();
            response.setSessionId(RandomStringUtils.randomAlphanumeric(RANDOM_LENGTH));
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<EuropeStopChargingResponse> stopCharging(@RequestBody EuropeStopChargingRequest request) {
        ResponseEntity responseEntity = validateRequest(request.getVehicleId());
        if (responseEntity == null) {
            EuropeStopChargingResponse response = EuropeStopChargingResponse.builder().build();
            if (INVALID_SESSION_ID.equals(request.getSessionId())) {
                response.setErrorCode(EuropeErrorCode.INVALID_SESSION_ID.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            response.setStatus("ACTIVE");
            return ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<EuropeRegistrationResponse> registerUser(EuropeRegistrationRequest request) {
        EuropeRegistrationResponse response = EuropeRegistrationResponse.builder().build();
        if (WRONG_CAR_TYPE.equals(request.getVehicle())) {
            response.setErrorCode(EuropeErrorCode.WRONG_CAR_TYPE.getErrorCode());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        response.setEmail(request.getEmail());
        response.setStatus("ACTIVE");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EuropeBaseResponse> deregisterUser(@RequestBody EuropeDeregistrationRequest request) {
        EuropeBaseResponse response = EuropeBaseResponse.builder().errorCode("100").build();
        if (request.getVehicle() != null) {
            switch (request.getVehicle()) {
                case WRONG_CAR_TYPE:
                    response.setErrorCode(EuropeErrorCode.WRONG_CAR_TYPE.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                case VEHICLE_NOT_FOUND:
                    response.setErrorCode(EuropeErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
                case WRONG_ACCESS_TOKEN:
                    response.setErrorCode(EuropeErrorCode.WRONG_ACCESS_TOKEN.getErrorCode());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
        }
        return ResponseEntity.ok(response);
    }

    private ResponseEntity validateRequest(String vehicleId) {
        EuropeBaseResponse response = EuropeBaseResponse.builder().build();
        switch (vehicleId) {
            case WRONG_ACCESS_TOKEN:
                response.setErrorCode(EuropeErrorCode.WRONG_ACCESS_TOKEN.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case STATION_NOT_AVAILABLE:
                response.setErrorCode(EuropeErrorCode.STATION_NOT_AVAILABLE.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case INTERNAL_SERVER_ERROR:
                response.setErrorCode(EuropeErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            case VEHICLE_NOT_FOUND:
                response.setErrorCode(EuropeErrorCode.VEHICLE_NOT_FOUND.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            case VEHICLE_NOT_CONNECTED:
                response.setErrorCode(EuropeErrorCode.VEHICLE_NOT_CONNECTED.getErrorCode());
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        return null;
    }
}

package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Alla Danko
 */
@Getter
@AllArgsConstructor
public enum CcsResponseStatus {

    //Successful responses
    CHARGING_STARTED(HttpStatus.OK, 100, "Charging successfully started"),
    CHARGING_STOPPED(HttpStatus.OK,101, "Charging successfully stopped"),
    SESSION_RETURNED(HttpStatus.OK,103, "Sessions returned"),
    USER_REGISTERED(HttpStatus.OK,104, "User successfully registered"),
    USER_DEREGISTERED(HttpStatus.OK,105, "User successfully deregistered"),
    USER_INFO_RETURNED(HttpStatus.OK,106, "User info returned"),

    //Errors from CCS
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, 300, "Parameter %s is missing"),
    USER_NOT_REGISTERED(HttpStatus.UNPROCESSABLE_ENTITY, 301, "User not registered"),
    VEHICLE_NOT_REGISTERED(HttpStatus.UNPROCESSABLE_ENTITY, 302, "Vehicle not registered"),
    NO_ACTIVE_SESSION(HttpStatus.UNPROCESSABLE_ENTITY, 303, "No active session found"),

    //Errors from source systems
    VEHICLE_NOT_CONNECTED(HttpStatus.UNPROCESSABLE_ENTITY, 400, "Vehicle not connected"),
    STATION_NOT_AVAILABLE(HttpStatus.UNPROCESSABLE_ENTITY, 401, "Station not available"),
    VEHICLE_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, 402, "Vehicle not found"),
    WRONG_PROVIDER_USER_ID(HttpStatus.UNPROCESSABLE_ENTITY, 403, "Wrong provider user id"),
    WRONG_ACCESS_TOKEN(HttpStatus.UNPROCESSABLE_ENTITY, 404, "Wrong access token"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, 405, "Unauthorized Access"),
    INVALID_SESSION_ID(HttpStatus.UNPROCESSABLE_ENTITY, 406, "Invalid session id"),
    WRONG_CAR_TYPE(HttpStatus.UNPROCESSABLE_ENTITY, 407, "Wrong car type"),

    //Critical errors
    UNKNOWN_RESOURCE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Unknown resource error"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 501, "Resource not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 502, "Internal server error");

    HttpStatus httpStatus;
    Integer responseCode;
    String message;

    @JsonValue
    public Integer getValue() {
        return responseCode;
    }

    @JsonCreator
    public static CcsResponseStatus fromValue(Integer value) {
        for (CcsResponseStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}

package com.emmilletta.ccs.client.japan;

import com.emmilletta.ccs.dto.CcsResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alla Danko
 */
@Getter
@AllArgsConstructor
public enum JapanErrorCode {

    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS", CcsResponseStatus.UNAUTHORIZED_ACCESS),
    VEHICLE_NOT_FOUND("VEHICLE_NOT_FOUND", CcsResponseStatus.VEHICLE_NOT_FOUND),
    VEHICLE_NOT_CONNECTED("VEHICLE_NOT_CONNECTED", CcsResponseStatus.VEHICLE_NOT_CONNECTED),
    NOT_SUPPORTED_CAR("NOT_SUPPORTED_CAR", CcsResponseStatus.WRONG_CAR_TYPE),
    WRONG_SESSION_ID("WRONG_SESSION_ID", CcsResponseStatus.INVALID_SESSION_ID),
    STATION_BLOCKED("STATION_BLOCKED", CcsResponseStatus.STATION_NOT_AVAILABLE),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", CcsResponseStatus.INTERNAL_SERVER_ERROR);

    String errorCode;
    CcsResponseStatus ccsResponseStatus;

    public static CcsResponseStatus getCcsResponseStatus(String errorCode) {
        for (JapanErrorCode error : values()) {
            if (error.getErrorCode().equals(errorCode)) {
                return error.getCcsResponseStatus();
            }
        }
        return null;
    }
}

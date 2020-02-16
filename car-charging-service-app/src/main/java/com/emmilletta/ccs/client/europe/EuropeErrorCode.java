package com.emmilletta.ccs.client.europe;

import com.emmilletta.ccs.dto.CcsResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alla Danko
 */
@Getter
@AllArgsConstructor
public enum EuropeErrorCode {

    WRONG_ACCESS_TOKEN("401", CcsResponseStatus.WRONG_ACCESS_TOKEN),
    VEHICLE_NOT_FOUND("404", CcsResponseStatus.VEHICLE_NOT_FOUND),
    VEHICLE_NOT_CONNECTED("408", CcsResponseStatus.VEHICLE_NOT_CONNECTED),
    WRONG_CAR_TYPE("415", CcsResponseStatus.WRONG_CAR_TYPE),
    INVALID_SESSION_ID("422", CcsResponseStatus.INVALID_SESSION_ID),
    STATION_NOT_AVAILABLE("468", CcsResponseStatus.STATION_NOT_AVAILABLE),
    INTERNAL_SERVER_ERROR("500", CcsResponseStatus.INTERNAL_SERVER_ERROR);

    String errorCode;
    CcsResponseStatus ccsResponseStatus;

    public static CcsResponseStatus getCcsResponseStatus(String errorCode) {
        for (EuropeErrorCode error : values()) {
            if (error.getErrorCode().equals(errorCode)) {
                return error.getCcsResponseStatus();
            }
        }
        return null;
    }
}

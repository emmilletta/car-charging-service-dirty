package com.emmilletta.ccs.client.asia;

import com.emmilletta.ccs.dto.CcsResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alla Danko
 */
@Getter
@AllArgsConstructor
public enum AsiaErrorCode {

    WRONG_PROVIDER_USER_ID("730021", CcsResponseStatus.WRONG_PROVIDER_USER_ID),
    VEHICLE_NOT_FOUND("750038", CcsResponseStatus.VEHICLE_NOT_FOUND),
    VEHICLE_NOT_CONNECTED("750040", CcsResponseStatus.VEHICLE_NOT_CONNECTED),
    WRONG_CAR_TYPE("750065", CcsResponseStatus.WRONG_CAR_TYPE),
    INVALID_SESSION_ID("710022", CcsResponseStatus.INVALID_SESSION_ID),
    STATION_NOT_AVAILABLE("780015", CcsResponseStatus.STATION_NOT_AVAILABLE),
    INTERNAL_SERVER_ERROR("780500", CcsResponseStatus.INTERNAL_SERVER_ERROR);

    String errorCode;
    CcsResponseStatus ccsResponseStatus;

    public static CcsResponseStatus getCcsResponseStatus(String errorCode) {
        for (AsiaErrorCode error : values()) {
            if (error.getErrorCode().equals(errorCode)) {
                return error.getCcsResponseStatus();
            }
        }
        return null;
    }
}

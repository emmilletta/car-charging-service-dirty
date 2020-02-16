package com.emmilletta.ccs.client.vis;

import com.emmilletta.ccs.dto.CcsResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alla Danko
 */
@Getter
@AllArgsConstructor
public enum VisErrorCode {

    VEHICLE_NOT_FOUND("VEHICLE_NOT_FOUND", CcsResponseStatus.VEHICLE_NOT_FOUND),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", CcsResponseStatus.INTERNAL_SERVER_ERROR);

    String errorCode;
    CcsResponseStatus ccsResponseStatus;

    public static CcsResponseStatus getCcsResponseStatus(String errorCode) {
        for (VisErrorCode error : values()) {
            if (error.getErrorCode().equals(errorCode)) {
                return error.getCcsResponseStatus();
            }
        }
        return null;
    }
}

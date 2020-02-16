package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Alla Danko
 */
public enum SessionStatus {

    ACTIVE,

    INACTIVE;

    @JsonCreator
    public static SessionStatus fromValue(String value) {
        for (SessionStatus type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}

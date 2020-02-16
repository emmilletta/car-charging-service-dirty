package com.emmilletta.ccs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Alla Danko
 */
public enum ProviderEnum {

    EUROPE,

    ASIA,

    JAPAN;

    @JsonCreator
    public static ProviderEnum fromValue(String value) {
        for (ProviderEnum type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}

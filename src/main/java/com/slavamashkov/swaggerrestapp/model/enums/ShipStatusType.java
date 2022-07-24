package com.slavamashkov.swaggerrestapp.model.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public enum ShipStatusType {
    PORT,
    SEA;

    public static ShipStatusType getStatusType(String statusInString) {
        for (ShipStatusType type : ShipStatusType.values()) {
            if (type.name().equals(statusInString)) {
                return type;
            }
        }

        return null;
    }
}

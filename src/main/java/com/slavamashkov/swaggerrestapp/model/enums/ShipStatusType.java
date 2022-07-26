package com.slavamashkov.swaggerrestapp.model.enums;

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

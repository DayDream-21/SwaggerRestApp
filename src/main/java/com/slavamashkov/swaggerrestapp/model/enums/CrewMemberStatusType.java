package com.slavamashkov.swaggerrestapp.model.enums;

public enum CrewMemberStatusType {
    ON_SHIP,
    ON_LAND;

    public static CrewMemberStatusType getStatusType(String statusInString) {
        for (CrewMemberStatusType status : CrewMemberStatusType.values()) {
            if (status.name().equals(statusInString)) {
                return status;
            }
        }

        return null;
    }
}

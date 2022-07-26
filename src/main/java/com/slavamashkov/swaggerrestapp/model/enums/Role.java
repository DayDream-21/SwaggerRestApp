package com.slavamashkov.swaggerrestapp.model.enums;

public enum Role {
    CAPITAN,
    MATE;

    public static Role getStatusType(String statusInString) {
        for (Role role : Role.values()) {
            if (role.name().equals(statusInString)) {
                return role;
            }
        }

        return null;
    }
}

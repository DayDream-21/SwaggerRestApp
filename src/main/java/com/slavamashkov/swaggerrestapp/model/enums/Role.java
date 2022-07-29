package com.slavamashkov.swaggerrestapp.model.enums;

public enum Role {
    CAPITAN,
    MATE;

    public static Role getRole(String roleInString) {
        for (Role role : Role.values()) {
            if (role.name().equals(roleInString)) {
                return role;
            }
        }

        return null;
    }
}

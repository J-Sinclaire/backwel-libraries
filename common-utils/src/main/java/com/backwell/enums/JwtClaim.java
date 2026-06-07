package com.backwell.enums;


public enum JwtClaim {
    USER_ID("user-id"),
    USER_UUID("user-uuid"),
    EMAIL("email"),
    ROLES("roles"),
    AUTH_PROVIDER("auth-provider");

    private final String key;

    JwtClaim(String key) {
        this.key = key;
    }

    public String key(){
        return key;
    }
}
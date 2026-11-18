package com.backwell.enums.permission;

public enum ActionType {
    META_PERMISSION("meta:permission"),
    JURISDICTION("jurisdiction"),
    ASSIGN_USER("assign-user"),
    LIST("list"),
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete");

    private final String code;

    ActionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

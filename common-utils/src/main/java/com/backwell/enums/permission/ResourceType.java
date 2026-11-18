package com.backwell.enums.permission;

public enum ResourceType {
    ROLES("roles"),
    USER("user"),
    CATEGORY("category"),
    PRODUCT("product"),
    ITEM("item"),
    STOCK("stock"),
    CUPON("cupon"),
    DISCOUNT("discount"),
    SALE("sale");

    private final String code;

    ResourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

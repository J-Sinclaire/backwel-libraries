package com.backwell.enums;

public enum Permission {
    USER_PERMISSIONS_GRANT("user:permissions:grant"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    CATEGORY_PERMISSIONS_GRANT("category:permissions:grant"),
    CATEGORY_CREATE("category:create"),
    CATEGORY_DELETE("category:delete"),
    CATEGORY_READ("category:read"),
    CATEGORY_UPDATE("category:update"),

    PRODUCT_PERMISSIONS_GRANT("product:permissions:grant"),
    PRODUCT_CREATE("product:create"),
    PRODUCT_DELETE("product:delete"),
    PRODUCT_READ("product:read"),
    PRODUCT_UPDATE("product:update"),

    ITEM_PERMISSIONS_GRANT("item:permissions:grant"),
    ITEM_CREATE("item:create"),
    ITEM_DELETE("item:delete"),
    ITEM_READ("item:read"),
    ITEM_UPDATE("item:update"),

    STOCK_PERMISSIONS_GRANT("stock:permissions:grant"),
    STOCK_READ("stock:read"),
    STOCK_UPDATE("stock:update"),
    STOCK_DELETE("stock:delete"),

    CUPON_PERMISSIONS_GRANT("cupon:permissions:grant"),
    CUPON_CREATE("cupon:create"),
    CUPON_DELETE("cupon:delete"),
    CUPON_READ("cupon:read"),
    CUPON_UPDATE("cupon:update"),

    DISCOUNT_PERMISSIONS_GRANT("discount:permissions:grant"),
    DISCOUNT_CREATE("discount:create"),
    DISCOUNT_DELETE("discount:delete"),
    DISCOUNT_READ("discount:read"),
    DISCOUNT_UPDATE("discount:update"),

    SALE_PERMISSIONS_GRANT("sale:permissions:grant"),
    SALE_CREATE("sale:create"),
    SALE_DELETE("sale:delete"),
    SALE_READ("sale:read");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

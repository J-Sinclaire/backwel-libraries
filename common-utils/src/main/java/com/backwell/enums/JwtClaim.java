package com.backwell.enums;

public enum JwtClaim {
    /**Account UUID*/
    SUB("sub"),
    EMAIL("email"),
    /**Base64 representation of permissions*/
    PERMISSIONS("p"),
    AUTHENTICATION_FACTORS("amr"),
    ;

    /** Representación textual de la clave del claim en el JSON del JWT. */
    private final String key;

    JwtClaim(String key) {
        this.key = key;
    }

    /**
     * Devuelve el identificador textual del claim para ser utilizado en el payload del JWT.
     *
     * @return El nombre del claim como {@link String}.
     */
    public String key() {
        return key;
    }
}
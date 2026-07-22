package com.backwell.enums;

import org.jspecify.annotations.NonNull;

import java.util.Map;

/**
 * Define los nombres estándar y personalizados de los *claims* (reivindicaciones)
 * contenidos dentro de los Tokens de JWT emitidos por el sistema.
 * <p>
 * Cada constante mapea un atributo legible a su correspondiente clave de texto
 * literal utilizada en la carga útil del JSON Web Token.
 * </p>
 *
 * @version 2.1.1
 */
public enum JwtClaim {

    /** Identificador numérico o secuencial del usuario en la base de datos. */
    USER_ID("user-id"),

    /** Identificador único universal (UUID) del usuario. */
    USER_UUID("user-uuid"),

    /** Dirección de correo electrónico del usuario titular del token. */
    EMAIL("email"),

    /** UUID del rol asignado al usuario*/
    ROLE_ID("role-id"),

    /** Rol asignado al usuario.*/
    ROLE("role"),

    /** Representación codificada de los permisos asociados al usuario (máscara de bits).
     * @since 2.0.0,
     * */
    PERMISSIONS("permissions"),

    /**
     * @since 2.1.1*/
    AUTHENTICATION_FACTORS("completed-authentication-factors"),
    ;

    /** Representación textual de la clave del claim en el JSON del JWT. */
    private final String key;

    /**
     * Constructor para asociar la clave textual del claim.
     *
     * @param key Clave literal que se escribirá en el JSON.
     */
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

    public void claim(@NonNull Map<String, Object> map, @NonNull Object value) {
        map.put(key, value);
    }
}
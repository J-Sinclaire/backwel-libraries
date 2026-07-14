package com.backwell.enums;

/**
 * Define los nombres estándar y personalizados de los *claims* (reivindicaciones)
 * contenidos dentro de los Tokens de JWT emitidos por el sistema.
 * <p>
 * Cada constante mapea un atributo legible a su correspondiente clave de texto
 * literal utilizada en la carga útil del JSON Web Token.
 * </p>
 *
 * @version 2.1.0
 */
public enum JwtClaim {

    /** Identificador numérico o secuencial del usuario en la base de datos. */
    USER_ID("user-id"),

    /** Identificador único universal (UUID) del usuario. */
    USER_UUID("user-uuid"),

    /** Dirección de correo electrónico del usuario titular del token. */
    EMAIL("email"),

    /** * Listado de roles asignados al usuario.
     * * @deprecated Reemplazado por {@link #ROLE} debido al cambio a un esquema
     * de rol único por usuario en la versión 2.0.0.
     */
    @Deprecated(forRemoval = true)
    ROLES("roles"),


    /** UUID del rol asignado al usuario*/
    ROLE_ID("role-id"),

    /** Rol asignado al usuario.*/
    ROLE("role"),

    /** Representación codificada de los permisos asociados al usuario (máscara de bits).
     * @since 2.0.0*/
    PERMISSIONS("permissions"),


    /** El proveedor a través del cual se autenticó el usuario. Ver {@link AuthProvider}.
     * @deprecated Replaced in favor of using {@link #AUTH_PROVIDERS} in the move towards an implementation with
     * more than one authentication provider per user. Deprecated as of version 2.1.0
     * */
    @Deprecated
    AUTH_PROVIDER("auth-provider"),

    /**
     * Lista de proveedores de autenticación del usuario en formato String
     * @since 2.1.0*/
    AUTH_PROVIDERS("auth-providers");

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
}
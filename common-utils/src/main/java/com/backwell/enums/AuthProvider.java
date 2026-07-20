package com.backwell.enums;

/**
 * Define los proveedores de autenticación soportados por el sistema.
 * <p>
 * Permite identificar si un usuario se ha autenticado de forma directa (credenciales locales)
 * o delegando la autenticación en un tercero de confianza.
 * </p>
 *
 * @version 1.0.0
 */
public enum AuthProvider {
    APPLE,
    /** Autenticación delegada a través del servicio de identidad de Google. */
    GOOGLE,
    /** Autenticación interna del sistema utilizando correo electrónico y contraseña local. */
    LOCAL
}
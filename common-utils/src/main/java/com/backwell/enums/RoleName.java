package com.backwell.enums;

import com.backwell.exception.UnknownRoleException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Representa los diferentes roles de usuario en el sistema y define los niveles
 * de jerarquía y permisos asociados a cada uno.
 * <p>
 * Los roles tienen un nivel numérico donde un menor valor (ej. 0) indica un
 * mayor privilegio, a excepción del rol {@link #USER} cuyo nivel es {@code null}.
 * </p>
 *
 * @version 1.0.0
 * @deprecated Este enum está obsoleto y se eliminará en la versión 2.0.0.
 * Se recomienda migrar al nuevo sistema de roles basado en base de datos.
 */
@Deprecated(forRemoval = true, since = "2.0.0")
public enum RoleName {

    /** Rol supremo del sistema con control total y máxima jerarquía. */
    OWNER(0),

    /** Rol de administrador con privilegios elevados. */
    ADMIN(100),

    /** Rol de gestión intermedio. */
    MANAGER(200),

    /** Rol de personal operativo del sistema. */
    STAFF(300),

    /** Rol de usuario final estándar. No posee un nivel numérico asignado. */
    USER(null);

    /** El nivel jerárquico del rol. Valores menores indican mayor jerarquía. */
    private final Integer level;

    /**
     * Constructor único para asignar el nivel jerárquico a cada rol.
     *
     * @param level Nivel numérico del rol (puede ser {@code null}).
     */
    RoleName(Integer level) {
        this.level = level;
    }

    /**
     * Verifica si este rol tiene la jerarquía suficiente en comparación con otro rol.
     * Un rol tiene rango sobre otro si su nivel numérico es mayor o igual (o si el rol comparado es USER).
     *
     * @param range El rol con el que se desea comparar la jerarquía.
     * @return {@code true} si este rol tiene igual o mayor jerarquía que el rol proporcionado;
     * {@code false} en caso contrario.
     */
    public boolean hasRange(RoleName range) {
        if (range == USER) return true;
        if (this.level == null) return false;
        return this.level >= range.level;
    }

    /**
     * Determina si este rol tiene permitido crear a un usuario con el rol objetivo especificado.
     *
     * @param targetRole El rol que se intenta crear.
     * @return {@code true} si tiene permisos para crearlo; {@code false} de lo contrario.
     */
    public boolean canCreate(RoleName targetRole) {
        if (this == OWNER) return true;
        if (this.level == null) return false;
        if (targetRole.level == null) return true;

        return this.level < targetRole.level;
    }

    /**
     * Determina si este rol tiene permitido revocar (eliminar o degradar) el rol objetivo especificado.
     *
     * @param targetRole El rol que se intenta revocar.
     * @return {@code true} si tiene permisos para revocarlo; {@code false} de lo contrario.
     * @throws NullPointerException si {@code targetRole} es {@code null}.
     */
    public boolean canRevoke(RoleName targetRole) {
        Objects.requireNonNull(targetRole, "Target role can't be null");

        if (this == OWNER) return true;
        if (targetRole == OWNER || this.level == null) return false;
        if (targetRole.level == null) return true;
        return this.level < targetRole.level;
    }

    /** Mapa de búsqueda rápida para obtener el enum a partir de su nombre en minúsculas. */
    private static final Map<String, RoleName> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(
                    r -> r.name().toLowerCase(),
                    Function.identity()
            ));

    /**
     * Obtiene el rol con mayor jerarquía (el nivel numérico más bajo) de un conjunto de roles dado.
     * Excluye los roles con nivel {@code null} a menos que no haya otra opción disponible.
     *
     * @param roles Conjunto de roles a evaluar.
     * @return El {@link RoleName} con mayor jerarquía, {@link #USER} si ningún rol válido es encontrado,
     * o {@code null} si el conjunto está vacío o es nulo.
     */
    public static RoleName getHighestRole(Set<RoleName> roles) {
        if (roles == null || roles.isEmpty()) return null;

        return roles.stream()
                .filter(r -> r.level != null)
                .min(Comparator.comparingInt(r -> r.level))
                .orElse(USER);
    }

    /**
     * Busca y retorna un {@link RoleName} basado en su nombre textual (insensible a mayúsculas/minúsculas).
     *
     * @param name El nombre del rol a buscar (ej. "admin", "Owner").
     * @return El {@link RoleName} correspondiente.
     * @throws UnknownRoleException si el nombre proporcionado no coincide con ningún rol existente.
     */
    public static RoleName fromString(String name) {
        RoleName role = LOOKUP.get(name.toLowerCase());
        if (role == null) throw new UnknownRoleException("Unknown role: " + name);
        return role;
    }
}
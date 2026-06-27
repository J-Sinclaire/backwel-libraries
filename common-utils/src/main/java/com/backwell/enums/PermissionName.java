package com.backwell.enums;

import java.util.*;

/**
 * Define los permisos granulares del sistema basados en recursos y acciones mediante
 * el formato {@code recurso:accion}.
 * <p>
 * Este enum implementa una optimización de alto rendimiento para el manejo de permisos mediante
 * máscaras de bits binarias. Al inicializarse la clase, los permisos se ordenan alfabéticamente
 * por su valor textual para garantizar que el índice binario asignado a cada permiso sea consistente,
 * determinista y agnóstico al orden de declaración física de las constantes.
 * </p>
 *
 * @version 2.0.0
 */
public enum PermissionName {

    ROLES_READ("roles:read"),
    ROLES_CREATE("roles:create"),
    ROLES_DELETE("roles:delete"),
    ROLES_PERMISSIONS_UPDATE("roles:permissions:update"),

    USER_ROLES_ASSIGN("user:roles:assign"),
    USER_ROLES_REVOKE("user:roles:revoke"),

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

    /** El valor en formato String del permiso (ej. "user:read"). */
    private final String value;

    /**
     * Constructor del permiso.
     *
     * @param value Cadena de texto que identifica de manera única el permiso.
     */
    PermissionName(String value) {
        this.value = value;
    }

    /**
     * Obtiene la cadena de texto descriptiva del permiso.
     *
     * @return El formato de cadena {@code recurso:accion}.
     */
    public String getValue() {
        return value;
    }

    /** Mapa interno que asocia cada constante de permiso con su respectiva posición en el vector binario. */
    private static final Map<PermissionName, Integer> PERMISSION_TO_INDEX = new HashMap<>();

    /** Arreglo ordenado alfabéticamente por {@code value} que sirve para resolver un permiso desde su índice binario. */
    private static final PermissionName[] INDEX_TO_PERMISSION_NAME;

    static {
        PermissionName[] sorted = Arrays.stream(values())
                .sorted(Comparator.comparing(PermissionName::getValue))
                .toArray(PermissionName[]::new);

        INDEX_TO_PERMISSION_NAME = sorted;

        for (int i = 0; i < sorted.length; i++) {
            PERMISSION_TO_INDEX.put(sorted[i], i);
        }
    }

    /**
     * Obtiene la posición (bit) asignada a este permiso en la máscara binaria global.
     *
     * @return El índice entero (0-indexado) basado en el ordenamiento alfabético de los permisos.
     */
    public int getBitIndex() {
        return PERMISSION_TO_INDEX.get(this);
    }

    /**
     * Resuelve una constante {@link PermissionName} a partir de su índice en la máscara binaria.
     *
     * @param index El índice numérico del bit.
     * @return El {@link PermissionName} correspondiente al índice provisto.
     * @throws IllegalArgumentException Si el índice es negativo o excede la cantidad total de permisos.
     */
    public static PermissionName fromBitIndex(int index) {
        if (index < 0 || index >= INDEX_TO_PERMISSION_NAME.length) {
            throw new IllegalArgumentException("Índice de permiso fuera de rango: " + index);
        }
        return INDEX_TO_PERMISSION_NAME[index];
    }

    /**
     * Compacta una colección de permisos en una única cadena hexadecimal que representa
     * una máscara de bits activa (Bitmask). Ideal para transmitir permisos de forma ligera en JWT.
     *
     * @param permissionNames Colección de permisos a codificar.
     * @return Una cadena en formato hexadecimal que representa los bits activos, o un {@code String}
     * vacío si la colección es nula o no contiene elementos.
     */
    public static String toHexBitMask(Collection<PermissionName> permissionNames) {
        if (permissionNames == null || permissionNames.isEmpty()) {
            return "";
        }

        BitSet bitSet = new BitSet(INDEX_TO_PERMISSION_NAME.length);
        for (PermissionName permissionName : permissionNames) {
            bitSet.set(permissionName.getBitIndex());
        }
        return HexFormat.of().formatHex(bitSet.toByteArray());
    }

    /**
     * Decodifica una cadena hexadecimal (Bitmask) y la reconstruye en un conjunto estructurado
     * de constantes {@link PermissionName}.
     *
     * @param hexBitmask Cadena hexadecimal que codifica los permisos activos.
     * @return Un {@link Set} mutable de tipo {@link EnumSet} con los permisos extraídos de la máscara,
     * o un conjunto vacío si la máscara es nula, vacía o contiene puros espacios en blanco.
     */
    public static Set<PermissionName> fromHexBitmask(String hexBitmask) {
        if (hexBitmask == null || hexBitmask.isBlank()) {
            return EnumSet.noneOf(PermissionName.class);
        }
        byte[] bytes = HexFormat.of().parseHex(hexBitmask);
        BitSet bitSet = BitSet.valueOf(bytes);

        Set<PermissionName> permissionNames = EnumSet.noneOf(PermissionName.class);
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            if (i < INDEX_TO_PERMISSION_NAME.length) {
                permissionNames.add(fromBitIndex(i));
            }
        }
        return permissionNames;
    }

    public static final Set<PermissionName> META_PERMISSIONS = Collections.unmodifiableSet(
            EnumSet.of(
                    ROLES_READ, ROLES_CREATE, ROLES_DELETE, ROLES_PERMISSIONS_UPDATE,
                    USER_ROLES_ASSIGN, USER_ROLES_REVOKE
            )
    );

    private static final String GRANT_SUFFIX = ":permissions:grant";

    public String getBaseResource() {
        return value.split(":")[0];
    }

    public boolean isGrantPermission() {
        return value.endsWith(GRANT_SUFFIX);
    }

    /**
     * Meta-permiso = controla la seguridad misma del sistema. Nunca es delegable,
     * sin importar cuántos permisos":grant" posea un actor. Se valida por membresía
     * explícita en META_PERMISSIONS (fuente de verdad) Y por prefijo como red de
     * seguridad ante futuras adiciones al namespace "roles:" o "user:roles:".
     */
    public boolean isMetaPermission() {
        return META_PERMISSIONS.contains(this)
                || isGrantPermission()
                || value.startsWith("roles:")
                || value.startsWith("user:roles:");
    }
}
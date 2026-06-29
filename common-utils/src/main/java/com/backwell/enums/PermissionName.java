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
 * @version 2.1.0
 */
public enum PermissionName {

    /**
     * Exclusivo de RRHH para crear roles que incluyen meta-permisos, supeditado a un ROLE OWNER,
     * intransferible a menos que sea un OWNER quien hace la request*/
    ROLES_META_CREATE("roles:meta-create"),
    /**
     * En presencia de un meta-permiso, permite a administradores de área crear roles subordinados los cuales solo
     * podrán contener roles dentro del scope de su meta-permiso y jamás incluir meta-permisos*/
    ROLES_CREATE("roles:create"),
    ROLES_READ("roles:read"),
    ROLES_UPDATE("roles:update"), // Includes the ability to update role-permissions
    ROLES_DELETE("roles:delete"),

    /**
     * En presencia de un meta-permiso, permite a administradores de área asignar roles existentes
     * (el set de permisos del rol designado debe de estar dentro del scope de los meta-permisos del propio
     * usuario y no puede incluir meta-permisos)*/
    ROLES_ASSIGN_USER("roles:assign-user"),

    USER_LIST("user:list"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    CATEGORY_META_PERMISSIONS_GRANT("category:permissions:grant"),
    CATEGORY_CREATE("category:create"),
    CATEGORY_DELETE("category:delete"),
    CATEGORY_READ("category:read"),
    CATEGORY_UPDATE("category:update"),

    PRODUCT_META_PERMISSIONS_GRANT("product:permissions:grant"),
    PRODUCT_CREATE("product:create"),
    PRODUCT_DELETE("product:delete"),
    PRODUCT_READ("product:read"),
    PRODUCT_UPDATE("product:update"),

    ITEM_META_PERMISSIONS_GRANT("item:permissions:grant"),
    ITEM_CREATE("item:create"),
    ITEM_DELETE("item:delete"),
    ITEM_READ("item:read"),
    ITEM_UPDATE("item:update"),

    STOCK_META_PERMISSIONS_GRANT("stock:permissions:grant"),
    STOCK_READ("stock:read"),
    STOCK_UPDATE("stock:update"),
    STOCK_DELETE("stock:delete"),


    CUPON_META_PERMISSIONS_GRANT("cupon:permissions:grant"),
    CUPON_CREATE("cupon:create"),
    CUPON_DELETE("cupon:delete"),
    CUPON_READ("cupon:read"),
    CUPON_UPDATE("cupon:update"),

    DISCOUNT_META_PERMISSIONS_GRANT("discount:permissions:grant"),
    DISCOUNT_CREATE("discount:create"),
    DISCOUNT_DELETE("discount:delete"),
    DISCOUNT_READ("discount:read"),
    DISCOUNT_UPDATE("discount:update"),

    SALE_META_PERMISSIONS_GRANT("sale:permissions:grant"),
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

    private static final String GRANT_SUFFIX = ":permissions:grant";
    private static final Map<PermissionName, Integer> PERMISSION_TO_INDEX = new HashMap<>();
    private static final Map<String, PermissionName> VALUE_TO_PERMISSION = new HashMap<>();
    private static final PermissionName[] INDEX_TO_PERMISSION_NAME;

    static {
        PermissionName[] sorted = Arrays.stream(values())
                .sorted(Comparator.comparing(PermissionName::getValue))
                .toArray(PermissionName[]::new);

        INDEX_TO_PERMISSION_NAME = sorted;

        for (int i = 0; i < sorted.length; i++) {
            PERMISSION_TO_INDEX.put(sorted[i], i);
            VALUE_TO_PERMISSION.put(sorted[i].getValue(), sorted[i]);
        }

    }

    /**
     * Obtiene la cadena de texto descriptiva del permiso.
     *
     * @return El formato de cadena {@code recurso:accion}.
     */
    public String getValue() {
        return value;
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

    public String getBaseResource() {
        return value.split(":")[0];
    }

    public boolean isGrantPermission() {
        return value.endsWith(GRANT_SUFFIX);
    }

    public boolean isMetaPermission() {
     return isGrantPermission() || value.startsWith("roles:");
    }

    /**
     * Resolves a constant {@link PermissionName} from its string representation.
     * <p>
     * This method uses an in-memory indexed search map that guarantees high-performance resolution in constant O(1) time.
     * </p>
     *
     * @param value The text string of the permission (e.g., "user:read").
     * @return The {@link PermissionName} corresponding to the provided value, wrapped in a {@link Optional}.
     * Returns {@link Optional#empty()} if the value is null, empty, or does not match any valid permission.
     */
    public static Optional<PermissionName> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_PERMISSION.get(value.trim()));
    }
}
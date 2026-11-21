package com.backwell.enums.permission;

import com.backwell.authorization.RoleMask;

import java.util.*;

public enum PermissionName {
    // =========================================================================
    // META-PERMISOS Y ROLES (Índices explícitos e inmutables)
    // =========================================================================
    ROLES_META_PERMISSION(ResourceType.ROLES, ActionType.META_PERMISSION, 0),
    ROLES_CREATE(ResourceType.ROLES, ActionType.CREATE, 1),
    ROLES_READ(ResourceType.ROLES, ActionType.READ, 2),
    ROLES_UPDATE(ResourceType.ROLES, ActionType.UPDATE, 3),
    ROLES_DELETE(ResourceType.ROLES, ActionType.DELETE, 4),
    ROLES_ASSIGN_USER(ResourceType.ROLES, ActionType.ASSIGN_USER, 5),

    // =========================================================================
    // PERMISOS OPERATIVOS SIN JURISDICCIÓN PROPIA
    // =========================================================================
    USER_LIST(ResourceType.USER, ActionType.LIST, 6),
    USER_READ(ResourceType.USER, ActionType.READ, 7),
    USER_UPDATE(ResourceType.USER, ActionType.UPDATE, 8),
    USER_CREATE(ResourceType.USER, ActionType.CREATE, 9),
    USER_DELETE(ResourceType.USER, ActionType.DELETE, 10),

    // =========================================================================
    // RECURSOS CON CONTROL DE ÁREA (JURISDICCIÓN)
    // =========================================================================
    CATEGORY_JURISDICTION(ResourceType.CATEGORY, ActionType.JURISDICTION, 11),
    CATEGORY_CREATE(ResourceType.CATEGORY, ActionType.CREATE, 12),
    CATEGORY_DELETE(ResourceType.CATEGORY, ActionType.DELETE, 13),
    CATEGORY_READ(ResourceType.CATEGORY, ActionType.READ, 14),
    CATEGORY_UPDATE(ResourceType.CATEGORY, ActionType.UPDATE, 15),

    PRODUCT_JURISDICTION(ResourceType.PRODUCT, ActionType.JURISDICTION, 16),
    PRODUCT_CREATE(ResourceType.PRODUCT, ActionType.CREATE, 17),
    PRODUCT_DELETE(ResourceType.PRODUCT, ActionType.DELETE, 18),
    PRODUCT_READ(ResourceType.PRODUCT, ActionType.READ, 19),
    PRODUCT_UPDATE(ResourceType.PRODUCT, ActionType.UPDATE, 20),

    ITEM_JURISDICTION(ResourceType.ITEM, ActionType.JURISDICTION, 21),
    ITEM_CREATE(ResourceType.ITEM, ActionType.CREATE, 22),
    ITEM_DELETE(ResourceType.ITEM, ActionType.DELETE, 23),
    ITEM_READ(ResourceType.ITEM, ActionType.READ, 24),
    ITEM_UPDATE(ResourceType.ITEM, ActionType.UPDATE, 25),

    STOCK_JURISDICTION(ResourceType.STOCK, ActionType.JURISDICTION, 26),
    STOCK_READ(ResourceType.STOCK, ActionType.READ, 27),
    STOCK_UPDATE(ResourceType.STOCK, ActionType.UPDATE, 28),
    STOCK_DELETE(ResourceType.STOCK, ActionType.DELETE, 29),

    CUPON_JURISDICTION(ResourceType.CUPON, ActionType.JURISDICTION, 30),
    CUPON_CREATE(ResourceType.CUPON, ActionType.CREATE, 31),
    CUPON_DELETE(ResourceType.CUPON, ActionType.DELETE, 32),
    CUPON_READ(ResourceType.CUPON, ActionType.READ, 33),
    CUPON_UPDATE(ResourceType.CUPON, ActionType.UPDATE, 34),

    DISCOUNT_JURISDICTION(ResourceType.DISCOUNT, ActionType.JURISDICTION, 35),
    DISCOUNT_CREATE(ResourceType.DISCOUNT, ActionType.CREATE, 36),
    DISCOUNT_DELETE(ResourceType.DISCOUNT, ActionType.DELETE, 37),
    DISCOUNT_READ(ResourceType.DISCOUNT, ActionType.READ, 38),
    DISCOUNT_UPDATE(ResourceType.DISCOUNT, ActionType.UPDATE, 39),

    SALE_JURISDICTION(ResourceType.SALE, ActionType.JURISDICTION, 40),
    SALE_CREATE(ResourceType.SALE, ActionType.CREATE, 41),
    SALE_DELETE(ResourceType.SALE, ActionType.DELETE, 42),
    SALE_READ(ResourceType.SALE, ActionType.READ, 43);

    private static final byte[] ALL_PERMISSIONS_MASK;
    private static final byte[] EMPTY_MASK = new byte[0];
    private static final Base64.Encoder B64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64_DECODER = Base64.getUrlDecoder();

    private static final Map<String, PermissionName> VALUE_TO_PERMISSION;
    private static final Map<ResourceType, Map<ActionType, PermissionName>> MATRIX;
    private static final Map<ResourceType, PermissionName> RESOURCE_JURISDICTIONS;
    private static final PermissionName[] INDEX_TO_PERMISSION;
    private static final int BYTE_ARRAY_SIZE;

    static {
        PermissionName[] all = values();
        int maxIndex = -1;
        Map<String, PermissionName> valueMap = new HashMap<>(all.length);
        Map<ResourceType, Map<ActionType, PermissionName>> matrix = new EnumMap<>(ResourceType.class);
        Map<ResourceType, PermissionName> jurisdictions = new EnumMap<>(ResourceType.class);

        for (PermissionName p : all) {
            if (p.bitIndex > maxIndex) {
                maxIndex = p.bitIndex;
            }
            if (valueMap.put(p.value, p) != null) {
                throw new IllegalStateException("Permiso duplicado: " + p.value);
            }

            Map<ActionType, PermissionName> actionMap =
                    matrix.computeIfAbsent(p.resource, k -> new EnumMap<>(ActionType.class));
            if (actionMap.put(p.action, p) != null) {
                throw new IllegalStateException("Combinación Resource/Action duplicada: " + p.value);
            }

            if (p.action == ActionType.JURISDICTION) {
                jurisdictions.put(p.resource, p);
            }
        }

        PermissionName[] indexArray = new PermissionName[maxIndex + 1];
        for (PermissionName p : all) {
            if (indexArray[p.bitIndex] != null) {
                throw new IllegalStateException("Índice de bit duplicado (" + p.bitIndex + ") en: " + p);
            }
            indexArray[p.bitIndex] = p;
        }

        matrix.replaceAll((res, actMap) -> Collections.unmodifiableMap(actMap));


        INDEX_TO_PERMISSION = indexArray;
        VALUE_TO_PERMISSION = Map.copyOf(valueMap);
        MATRIX = Collections.unmodifiableMap(matrix);
        RESOURCE_JURISDICTIONS = Collections.unmodifiableMap(jurisdictions);
        BYTE_ARRAY_SIZE = (maxIndex >>> 3) + 1;

        byte[] allMask = new byte[BYTE_ARRAY_SIZE];
        for (PermissionName p : all) {
            allMask[p.byteOffset] |= p.bitMask;
        }
        ALL_PERMISSIONS_MASK = allMask;
    }

    private final ResourceType resource;
    private final ActionType action;
    private final String value;
    private final int bitIndex;
    private final int byteOffset;
    private final byte bitMask;

    PermissionName(ResourceType resource, ActionType action, int bitIndex) {
        if (bitIndex < 0) {
            throw new IllegalArgumentException("El bitIndex no puede ser negativo: " + bitIndex);
        }
        this.resource = Objects.requireNonNull(resource, "resource es requerido");
        this.action = Objects.requireNonNull(action, "action es requerido");
        this.value = resource.getCode() + ":" + action.getCode();
        this.bitIndex = bitIndex;
        this.byteOffset = bitIndex >>> 3;
        this.bitMask = (byte) (1 << (bitIndex & 7));
    }

    public ResourceType getResource() {
        return resource;
    }

    public ActionType getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }

    public int getBitIndex() {
        return bitIndex;
    }

    public boolean isMetaPermission() {
        return this == ROLES_META_PERMISSION;
    }

    public boolean isJurisdiction() {
        return this.action == ActionType.JURISDICTION;
    }

    public boolean isActionMetaPermission() {
        return this.resource == ResourceType.ROLES && this.action != ActionType.META_PERMISSION;
    }

    /**
     * Obtiene el permiso de jurisdicción asociado al recurso de este permiso, si existe.
     * Ej: PRODUCT_CREATE.getRequiredJurisdiction() -> Optional.of(PRODUCT_JURISDICTION).
     */
    public Optional<PermissionName> getRequiredJurisdiction() {
        return Optional.ofNullable(RESOURCE_JURISDICTIONS.get(this.resource));
    }

    public static byte[] getAllPermissionsMask(){
        return ALL_PERMISSIONS_MASK.clone();
    }

    /**
     * Busca un permiso por su combinación exacta de Recurso y Acción en O(1) usando EnumMap.
     */
    public static Optional<PermissionName> of(ResourceType resource, ActionType action) {
        Map<ActionType, PermissionName> actions = MATRIX.get(resource);
        return actions == null ? Optional.empty() : Optional.ofNullable(actions.get(action));
    }

    /**
     * Devuelve todos los permisos definidos para un recurso dado (incluyendo su jurisdicción).
     */
    public static Set<PermissionName> forResource(ResourceType resource) {
        Map<ActionType, PermissionName> actions = MATRIX.get(resource);
        if (actions == null || actions.isEmpty()) {
            return EnumSet.noneOf(PermissionName.class);
        }
        return EnumSet.copyOf(actions.values());
    }

    public static PermissionName fromBitIndex(int index) {
        if (index < 0 || index >= INDEX_TO_PERMISSION.length || INDEX_TO_PERMISSION[index] == null) {
            throw new IllegalArgumentException("Índice de permiso inválido o deprecado: " + index);
        }
        return INDEX_TO_PERMISSION[index];
    }

    public static Optional<PermissionName> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_PERMISSION.get(value.trim()));
    }

    // =========================================================================
    // SERIALIZACIÓN Y EVALUACIÓN DE BITMASK (Base64URL <-> byte[])
    // =========================================================================

    public boolean isPresentIn(byte[] rawMask) {
        return rawMask != null
                && byteOffset < rawMask.length
                && (rawMask[byteOffset] & bitMask) != 0;
    }

    public static byte[] toByteArray(Collection<PermissionName> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return EMPTY_MASK;
        }
        byte[] bytes = new byte[BYTE_ARRAY_SIZE];
        int maxUsedByte = 0;
        for (PermissionName p : permissions) {
            bytes[p.byteOffset] |= p.bitMask;
            if (p.byteOffset > maxUsedByte) {
                maxUsedByte = p.byteOffset;
            }
        }
        return (maxUsedByte + 1 == bytes.length) ? bytes : Arrays.copyOf(bytes, maxUsedByte + 1);
    }

    public static EnumSet<PermissionName> fromByteArray(byte[] bytes) {
        EnumSet<PermissionName> result = EnumSet.noneOf(PermissionName.class);
        if (bytes == null || bytes.length == 0) {
            return result;
        }
        int limit = Math.min(bytes.length, BYTE_ARRAY_SIZE);
        for (int byteIdx = 0; byteIdx < limit; byteIdx++) {
            int unsignedByte = bytes[byteIdx] & 0xFF;
            while (unsignedByte != 0) {
                int bitInByte = Integer.numberOfTrailingZeros(unsignedByte);
                int globalIndex = (byteIdx << 3) + bitInByte;
                if (globalIndex < INDEX_TO_PERMISSION.length && INDEX_TO_PERMISSION[globalIndex] != null) {
                    result.add(INDEX_TO_PERMISSION[globalIndex]);
                }
                unsignedByte &= (unsignedByte - 1);
            }
        }
        return result;
    }

    public static RoleMask buildMask(Collection<PermissionName> permissions) {
        byte[] bytes = toByteArray(permissions);
        String base64 = toBase64BitMask(bytes);
        return new RoleMask(bytes, base64);
    }

    private static String toBase64BitMask(byte[] bytes) {
        Objects.requireNonNull(bytes, "Raw bytes must not be null.");
        return bytes.length == 0 ? "" : B64_ENCODER.encodeToString(bytes);
    }

    public static String toBase64BitMask(Collection<PermissionName> permissions) {
        byte[] bytes = toByteArray(permissions);
        return toBase64BitMask(bytes);
    }

    public static byte[] decodeBase64ToBytes(String base64Bitmask) {
        if (base64Bitmask == null || base64Bitmask.isBlank()) {
            return EMPTY_MASK;
        }
        return B64_DECODER.decode(base64Bitmask);
    }

    public static EnumSet<PermissionName> fromBase64BitMask(String base64Bitmask) {
        return fromByteArray(decodeBase64ToBytes(base64Bitmask));
    }



    // =========================================================================
    // OPERACIONES MASIVAS A NIVEL DE BITS (Zero-Allocation en lectura)
    // =========================================================================

    /**
     * Compara semánticamente dos máscaras de permisos a nivel de bits.
     * Ignora bytes en cero al final y considera equivalentes {@code null},
     * arreglos vacíos y arreglos compuestos únicamente por ceros.
     *
     * @param maskA Primera máscara.
     * @param maskB Segunda máscara.
     * @return {@code true} si ambas máscaras representan exactamente los mismos permisos activos.
     */
    public static boolean equals(byte[] maskA, byte[] maskB) {
        if (maskA == maskB) {
            return true;
        }

        int lenA = (maskA == null) ? 0 : maskA.length;
        int lenB = (maskB == null) ? 0 : maskB.length;
        int commonLen = Math.min(lenA, lenB);

        // 1. Compara la porción compartida
        for (int i = 0; i < commonLen; i++) {
            if (maskA[i] != maskB[i]) {
                return false;
            }
        }

        // 2. Si maskA es más largo, sus bytes excedentes deben ser 0
        for (int i = commonLen; i < lenA; i++) {
            if (maskA[i] != 0) {
                return false;
            }
        }

        // 3. Si maskB es más largo, sus bytes excedentes deben ser 0
        for (int i = commonLen; i < lenB; i++) {
            if (maskB[i] != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica si {@code userMask} contiene TODOS los bits encendidos en {@code requiredMask}.
     * Ideal para validar múltiples permisos requeridos en un solo paso O(1)/O(N bytes).
     *
     * @param userMask     Máscara de permisos del usuario.
     * @param requiredMask Máscara de permisos requeridos (puede precalcularse una vez como constante).
     * @return {@code true} si todos los permisos requeridos están presentes en {@code userMask}.
     */
    public static boolean hasAll(byte[] userMask, byte[] requiredMask) {
        if (requiredMask == null || requiredMask.length == 0) {
            return true;
        }
        if (userMask == null || userMask.length == 0) {
            // Solo es válido si requiredMask tiene puros bytes en 0
            for (byte req : requiredMask) {
                if (req != 0) {
                    return false;
                }
            }
            return true;
        }

        int commonLen = Math.min(userMask.length, requiredMask.length);
        for (int i = 0; i < commonLen; i++) {
            int req = requiredMask[i] & 0xFF;
            if ((userMask[i] & req) != req) {
                return false;
            }
        }

        // Si requiredMask es más largo que userMask, los bytes excedentes deben ser 0
        for (int i = commonLen; i < requiredMask.length; i++) {
            if (requiredMask[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si {@code userMask} contiene AL MENOS UNO de los bits encendidos en {@code candidateMask}.
     *
     * @param userMask      Máscara de permisos del usuario.
     * @param candidateMask Máscara con los permisos candidatos.
     * @return {@code true} si existe al menos un permiso en común.
     */
    public static boolean hasAny(byte[] userMask, byte[] candidateMask) {
        if (userMask == null || userMask.length == 0 || candidateMask == null || candidateMask.length == 0) {
            return false;
        }
        int commonLen = Math.min(userMask.length, candidateMask.length);
        for (int i = 0; i < commonLen; i++) {
            if ((userMask[i] & candidateMask[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Combina (unión lógica OR) dos máscaras de permisos en una nueva máscara compacta.
     * Útil para sumar permisos de múltiples roles asignados a un mismo usuario antes de emitir el JWT.
     *
     * @param maskA Primera máscara.
     * @param maskB Segunda máscara.
     * @return Nueva máscara recortada sin bytes en cero al final.
     */
    public static byte[] merge(byte[] maskA, byte[] maskB) {
        boolean emptyA = (maskA == null || maskA.length == 0);
        boolean emptyB = (maskB == null || maskB.length == 0);

        if (emptyA && emptyB) {
            return EMPTY_MASK;
        }
        if (emptyA) {
            return trimTrailingZeros(maskB);
        }
        if (emptyB) {
            return trimTrailingZeros(maskA);
        }

        int maxLen = Math.min(Math.max(maskA.length, maskB.length), BYTE_ARRAY_SIZE);
        byte[] result = new byte[maxLen];
        int lastNonZero = -1;

        for (int i = 0; i < maxLen; i++) {
            byte a = i < maskA.length ? maskA[i] : 0;
            byte b = i < maskB.length ? maskB[i] : 0;
            byte combined = (byte) (a | b);
            result[i] = combined;
            if (combined != 0) {
                lastNonZero = i;
            }
        }

        if (lastNonZero == -1) {
            return EMPTY_MASK;
        }
        return (lastNonZero + 1 == result.length) ? result : Arrays.copyOf(result, lastNonZero + 1);
    }

    /**
     * Revoca permisos (diferencia lógica {@code baseMask & ~revokeMask}) devolviendo una nueva máscara compacta.
     * Útil para aplicar restricciones o denegaciones explícitas sobre un rol base.
     *
     * @param baseMask   Máscara original.
     * @param revokeMask Máscara con los permisos a eliminar.
     * @return Nueva máscara sin los permisos revocados.
     */
    public static byte[] subtract(byte[] baseMask, byte[] revokeMask) {
        if (baseMask == null || baseMask.length == 0) {
            return EMPTY_MASK;
        }
        if (revokeMask == null || revokeMask.length == 0) {
            return trimTrailingZeros(baseMask);
        }

        int len = Math.min(baseMask.length, BYTE_ARRAY_SIZE);
        byte[] result = new byte[len];
        int lastNonZero = -1;

        for (int i = 0; i < len; i++) {
            byte rev = i < revokeMask.length ? revokeMask[i] : 0;
            byte filtered = (byte) (baseMask[i] & ~rev);
            result[i] = filtered;
            if (filtered != 0) {
                lastNonZero = i;
            }
        }

        if (lastNonZero == -1) {
            return EMPTY_MASK;
        }
        return (lastNonZero + 1 == result.length) ? result : Arrays.copyOf(result, lastNonZero + 1);
    }

    private static byte[] trimTrailingZeros(byte[] mask) {
        int limit = Math.min(mask.length, BYTE_ARRAY_SIZE);
        int lastNonZero = limit - 1;
        while (lastNonZero >= 0 && mask[lastNonZero] == 0) {
            lastNonZero--;
        }
        return lastNonZero < 0 ? EMPTY_MASK : Arrays.copyOf(mask, lastNonZero + 1);
    }
}
package com.backwell.enums;

import com.backwell.exception.UnknownRoleException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RoleName{
    OWNER(0),
    ADMIN(100),
    MANAGER(200),
    STAFF(300),
    USER(null);
    private final Integer level;

    RoleName(Integer level) {
        this.level = level;
    }


    public boolean hasRange(RoleName range) {
        if (range == USER) return true;
        if (this.level == null) return false;
        return this.level >= range.level;
    }

    public boolean canCreate(RoleName targetRole) {
        if (this == OWNER) return true;
        if (this.level == null) return false;
        if(targetRole.level == null) return true;

        return this.level < targetRole.level;
    }

    public boolean canRevoke(RoleName targetRole) {
        Objects.requireNonNull(targetRole, "Target role can't be null");

        if (this == OWNER) return true;
        if (targetRole == OWNER || this.level == null) return false;
        if (targetRole.level == null) return true;
        return this.level < targetRole.level;
    }

    private static final Map<String, RoleName> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(
                    r->r.name().toLowerCase(),
                    Function.identity()
            ));


    public static RoleName getHighestRole(Set<RoleName> roles) {
        if  (roles == null || roles.isEmpty()) return null;

        return roles.stream()
                .filter(r-> r.level != null)
                .min(Comparator.comparingInt(r-> r.level))
                .orElse(USER);
    }

    public static RoleName fromString(String name) {
        RoleName role = LOOKUP.get(name.toLowerCase());
        if (role == null) throw new UnknownRoleException("Unknown role: " + name);
        return role;
    }
}
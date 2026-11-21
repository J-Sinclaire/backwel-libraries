package com.backwell.authorization;

public record RoleMask  (
        byte[] rawRoles,
        String base64x
) {}

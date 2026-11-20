package com.backwell.authorization;

import java.util.UUID;

public record RoleMask  (
        byte[] rawRoles,
        String base64x
) {}

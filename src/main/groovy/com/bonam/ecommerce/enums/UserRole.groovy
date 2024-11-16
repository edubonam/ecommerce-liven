package com.bonam.ecommerce.enums

enum UserRole {
    ADMIN,
    USER

    static UserRole fromString(String role) {
        try {
            valueOf(role.toUpperCase())
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role")
        }
    }

}
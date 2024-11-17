package com.bonam.ecommerce.enums

enum OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED

    static OrderStatus fromString(String status) {
        try {
            valueOf(status.toUpperCase())
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OrderStatus")
        }
    }
}
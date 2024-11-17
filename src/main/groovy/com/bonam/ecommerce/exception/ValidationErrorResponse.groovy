package com.bonam.ecommerce.exception

class ValidationErrorResponse {
    String field
    String errorMessage

    ValidationErrorResponse(String field, String errorMessage) {
        this.field = field
        this.errorMessage = errorMessage
    }
}

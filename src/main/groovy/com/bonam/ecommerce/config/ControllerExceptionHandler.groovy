package com.bonam.ecommerce.config

import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.exception.ValidationErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException)
    ResponseEntity<List<ValidationErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse> errors = ex.bindingResult.fieldErrors.collect { FieldError error ->
            new ValidationErrorResponse(error.field, error.defaultMessage)
        }

        new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CustomException)
    ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
        Map<String, String> response = new HashMap<>()
        response.put("error", ex.message)

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception)
    ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>()
        response.put("error", "unexpected error: " + ex.message)

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

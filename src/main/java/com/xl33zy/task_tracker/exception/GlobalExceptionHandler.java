package com.xl33zy.task_tracker.exception;

import com.xl33zy.task_tracker.dto.ApiResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String requestId = generateRequestId();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(
                        404,
                        "Not Found",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", ""),
                        requestId,
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst().orElse("Validation failed");
        String requestId = generateRequestId();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(
                        400,
                        "Validation Error",
                        msg,
                        request.getDescription(false).replace("uri=", ""),
                        requestId,
                        null
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String requestId = generateRequestId();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseDTO.error(
                                     400,
                                     "Validation Error",
                                     ex.getMessage(),
                                     request.getDescription(false).replace("uri=", ""),
                                     requestId,
                                     null
                             ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<?>> handleGeneralException(Exception ex, WebRequest request) {
        String requestId = generateRequestId();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponseDTO.error(
                                     500,
                                     "Internal Server Error",
                                     "Something went wrong",
                                     request.getDescription(false).replace("uri=", ""),
                                     requestId,
                                     ex.getMessage()
                             ));
    }
}

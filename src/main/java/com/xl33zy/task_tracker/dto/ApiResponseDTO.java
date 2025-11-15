package com.xl33zy.task_tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponseDTO<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String error;
    private T data;
    private String path;
    private String requestId;
    private String details;

    public static <T> ApiResponseDTO<T> success(T data, String message, String path, String requestId) {
        return ApiResponseDTO.<T>builder()
                             .timestamp(LocalDateTime.now())
                             .status(200)
                             .message(message)
                             .error(null)
                             .data(data)
                             .path(path)
                             .requestId(requestId)
                             .build();
    }

    public static ApiResponseDTO<?> error(int status, String error, String message, String path, String requestId, String details) {
        return ApiResponseDTO.builder()
                             .timestamp(LocalDateTime.now())
                             .status(status)
                             .error(error)
                             .message(message)
                             .data(null)
                             .path(path)
                             .requestId(requestId)
                             .details(details)
                             .build();
    }
}

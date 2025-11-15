package com.xl33zy.task_tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for task response")
public class TaskResponse {

    @Schema(description = "Task ID", example = "1")
    private Long id;

    @Schema(description = "Task title", example = "Clean room")
    private String title;

    @Schema(description = "Task description", example = "Vacuum and mop the floor")
    private String description;

    @Schema(description = "Task status", example = "NEW")
    private TaskStatus status;

    @Schema(description = "Task priority", example = "MEDIUM")
    private TaskPriority priority;

    @Schema(description = "Task creation date", example = "2025-11-11 15:21:35")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Task last update date", example = "2025-11-11 15:21:35")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

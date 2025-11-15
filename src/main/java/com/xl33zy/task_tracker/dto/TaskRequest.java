package com.xl33zy.task_tracker.dto;

import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for creating a new task")
public class TaskRequest {
    @NotBlank(message = "Title cannot be empty")
    @Schema(description = "Task title", example = "Clean room")
    String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Task description", example = "Vacuum and mop the floor")
    String description;

    @Builder.Default
    @Schema(description = "Task status", example = "NEW")
    TaskStatus status = TaskStatus.NEW;

    @Builder.Default
    @Schema(description = "Task priority", example = "MEDIUM")
    private TaskPriority priority = TaskPriority.MEDIUM; // default
}

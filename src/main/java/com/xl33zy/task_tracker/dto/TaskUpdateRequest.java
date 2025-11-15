package com.xl33zy.task_tracker.dto;

import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Task title", example = "Clean room")
public class TaskUpdateRequest {
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Task description", example = "Vacuum and mop the floor")
    private String description;

    @Schema(description = "Task status", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "Task priority", example = "MEDIUM")
    private TaskPriority priority;
}

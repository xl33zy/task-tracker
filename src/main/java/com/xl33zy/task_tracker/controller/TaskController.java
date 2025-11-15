package com.xl33zy.task_tracker.controller;

import com.xl33zy.task_tracker.dto.ApiResponseDTO;
import com.xl33zy.task_tracker.dto.TaskRequest;
import com.xl33zy.task_tracker.dto.TaskResponse;
import com.xl33zy.task_tracker.dto.TaskUpdateRequest;
import com.xl33zy.task_tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import java.util.List;
import java.util.UUID;

@Tag(name = "Tasks", description = "CRUD operations for tasks")
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Tasks created")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request, WebRequest webRequest) {
        log.info("Creating new task with title: {}", request.getTitle());
        TaskResponse response = taskService.createTask(request);
        String requestId = generateRequestId();
        return ResponseEntity.status(201)
                             .body(ApiResponseDTO.success(response, "Task created successfully", webRequest.getDescription(false).replace("uri=", ""), requestId));
    }

    @Operation(summary = "Get all tasks", description = "Returns list of tasks")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TaskResponse>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            WebRequest webRequest
    ) {
        List<TaskResponse> tasks = taskService.getAllTasks(page, size, sort, status, priority);
        String requestId = generateRequestId();
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        tasks,
                        "Tasks retrieved successfully",
                        webRequest.getDescription(false).replace("uri=", ""),
                        requestId
                )
        );
    }

    @Operation(summary = "Get task by ID")
    @ApiResponse(responseCode = "200", description = "Task retrieved")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponse>> getTaskById(@PathVariable Long id, WebRequest webRequest) {
        TaskResponse response = taskService.getTaskById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        String requestId = generateRequestId();
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Task retrieved successfully", webRequest.getDescription(false).replace("uri=", ""), requestId));
    }

    @Operation(summary = "Update task partially")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponse>> patchTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request, WebRequest webRequest) {
        TaskResponse response = taskService.patchTask(id, request)
                                           .orElseThrow(() -> new EntityNotFoundException("Task not found for update with id: " + id));
        String requestId = generateRequestId();
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Task updated successfully", webRequest.getDescription(false).replace("uri=", ""), requestId));
    }

    @Operation(summary = "Delete task by ID")
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteTask(@PathVariable Long id, WebRequest webRequest) {
        String requestId = generateRequestId();
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponseDTO.success(null, "Task deleted successfully", webRequest.getDescription(false).replace("uri=",""), requestId));
        } else {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
    }
}

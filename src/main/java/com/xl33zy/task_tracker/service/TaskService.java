package com.xl33zy.task_tracker.service;

import com.xl33zy.task_tracker.dto.TaskRequest;
import com.xl33zy.task_tracker.dto.TaskResponse;
import com.xl33zy.task_tracker.dto.TaskUpdateRequest;
import com.xl33zy.task_tracker.mapper.TaskMapper;
import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import com.xl33zy.task_tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import com.xl33zy.task_tracker.model.Task;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = taskMapper.toEntity(taskRequest);
        Task saved = taskRepository.save(task);
        System.out.println("createdAt: " + saved.getCreatedAt());
        System.out.println("updatedAt: " + saved.getUpdatedAt());
        return taskMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(int page, int size, String sort, String status, String priority) {
        Sort sortObj;

        if (sort == null || sort.isBlank()) {
            sortObj = Sort.by(Sort.Direction.ASC, "id");
        } else {
            String[] parts = sort.split(",");
            String property = parts[0].trim();
            Sort.Direction direction = parts.length > 1
                    ? Sort.Direction.fromString(parts[1].trim())
                    : Sort.Direction.ASC;
            sortObj = Sort.by(direction, property);
        }

        Pageable pageable = PageRequest.of(page, size, sortObj);

        TaskStatus taskStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                taskStatus = TaskStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + status);
            }
        }

        TaskPriority taskPriority = null;
        if (priority != null && !priority.isBlank()) {
            try {
                taskPriority = TaskPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid priority: " + priority);
            }
        }

        if (taskStatus != null && taskPriority != null) {
            return taskRepository.findByStatusAndPriority(taskStatus, taskPriority, pageable)
                                 .stream()
                                 .map(taskMapper::toResponse)
                                 .toList();
        } else if (taskStatus != null) {
            return taskRepository.findByStatus(taskStatus, pageable)
                                 .stream()
                                 .map(taskMapper::toResponse)
                                 .toList();
        } else if (taskPriority != null) {
            return taskRepository.findByPriority(taskPriority, pageable)
                                 .stream()
                                 .map(taskMapper::toResponse)
                                 .toList();
        } else {
            return taskRepository.findAll(pageable)
                                 .stream()
                                 .map(taskMapper::toResponse)
                                 .toList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<TaskResponse> getTaskById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toResponse);
    }

    @Transactional
    public Optional<TaskResponse> patchTask(Long id, TaskUpdateRequest request) {
        return taskRepository.findById(id).map(task -> {
            taskMapper.updateTaskFromRequest(request, task);
            Task updated = taskRepository.save(task);
            return taskMapper.toResponse(updated);
        });
    }

    @Transactional
    public boolean deleteTask(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            taskRepository.delete(taskOpt.get());
            return true;
        }
        return false;
    }
}

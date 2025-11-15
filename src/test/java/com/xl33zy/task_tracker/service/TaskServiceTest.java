package com.xl33zy.task_tracker.service;

import com.xl33zy.task_tracker.dto.TaskRequest;
import com.xl33zy.task_tracker.dto.TaskResponse;
import com.xl33zy.task_tracker.dto.TaskUpdateRequest;
import com.xl33zy.task_tracker.mapper.TaskMapper;
import com.xl33zy.task_tracker.model.Task;
import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import com.xl33zy.task_tracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task = Task.builder()
                   .id(1L)
                   .title("Test Task")
                   .description("Desc")
                   .status(TaskStatus.NEW)
                   .priority(TaskPriority.MEDIUM)
                   .build();

        taskResponse = TaskResponse.builder()
                                   .id(1L)
                                   .title("Test Task")
                                   .description("Desc")
                                   .status(TaskStatus.NEW)
                                   .priority(TaskPriority.MEDIUM)
                                   .build();
    }

    // ---------------- createTask ----------------
    @Test
    void createTask_savesAndReturnsTaskResponse() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.NEW);
        request.setPriority(TaskPriority.MEDIUM);

        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.createTask(request);

        assertThat(result).isEqualTo(taskResponse);
        verify(taskRepository).save(task);
        verify(taskMapper).toResponse(task);
    }

    // ---------------- getAllTasks ----------------
    @Test
    void getAllTasks_returnsPagedResponses() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        List<TaskResponse> results = taskService.getAllTasks(0, 10, null);

        assertThat(results).hasSize(1).contains(taskResponse);
        verify(taskRepository).findAll(pageable);
    }

    @Test
    void getAllTasks_withSort_returnsSortedResults() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "title"));
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        List<TaskResponse> results = taskService.getAllTasks(0, 10, "title,desc");

        assertThat(results).hasSize(1).contains(taskResponse);
        verify(taskRepository).findAll(pageable);
    }

    // ---------------- getTaskById ----------------
    @Test
    void getTaskById_existing_returnsResponse() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        Optional<TaskResponse> result = taskService.getTaskById(1L);

        assertThat(result).isPresent().contains(taskResponse);
    }

    @Test
    void getTaskById_nonExisting_returnsEmpty() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<TaskResponse> result = taskService.getTaskById(2L);

        assertThat(result).isEmpty();
    }

    // ---------------- patchTask ----------------
    @Test
    void patchTask_existing_updatesAndReturnsResponse() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        doAnswer(invocation -> {
            Task t = invocation.getArgument(1);
            t.setTitle(request.getTitle());
            return null;
        }).when(taskMapper).updateTaskFromRequest(request, task);

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        Optional<TaskResponse> result = taskService.patchTask(1L, request);

        assertThat(result).isPresent().contains(taskResponse);
        verify(taskMapper).updateTaskFromRequest(request, task);
        verify(taskRepository).save(task);
    }

    @Test
    void patchTask_nonExisting_returnsEmpty() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<TaskResponse> result = taskService.patchTask(999L, request);

        assertThat(result).isEmpty();
    }

    // ---------------- deleteTask ----------------
    @Test
    void deleteTask_existing_deletesAndReturnsTrue() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskService.deleteTask(1L);

        assertThat(result).isTrue();
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_nonExisting_returnsFalse() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = taskService.deleteTask(999L);

        assertThat(result).isFalse();
        verify(taskRepository, never()).delete(any());
    }
}

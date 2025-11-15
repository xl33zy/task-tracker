package com.xl33zy.task_tracker.controller;

import com.xl33zy.task_tracker.model.Task;
import com.xl33zy.task_tracker.model.TaskPriority;
import com.xl33zy.task_tracker.model.TaskStatus;
import com.xl33zy.task_tracker.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("tasktracker")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        task1 = Task.builder()
                    .title("Task 1")
                    .description("Desc 1")
                    .status(TaskStatus.NEW)
                    .priority(TaskPriority.MEDIUM)
                    .build();

        task2 = Task.builder()
                    .title("Task 2")
                    .description("Desc 2")
                    .status(TaskStatus.IN_PROGRESS)
                    .priority(TaskPriority.HIGH)
                    .build();

        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    // ---------------- GET /api/tasks ----------------
    @Test
    void getAllTasks_noParams_returnsDefaultPagination() throws Exception {
        mockMvc.perform(get("/api/tasks"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void getAllTasks_withPagination_returnsCorrectPage() throws Exception {
        mockMvc.perform(get("/api/tasks")
                       .param("page", "0")
                       .param("size", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void getAllTasks_withSort_returnsSortedTasks() throws Exception {
        mockMvc.perform(get("/api/tasks")
                       .param("sort", "title,desc"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data[0].title", is("Task 2")));
    }

    // ---------------- GET /api/tasks/{id} ----------------
    @Test
    void getTaskById_existing_returnsTask() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", task1.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data.title", is(task1.getTitle())));
    }

    @Test
    void getTaskById_nonExisting_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999))
               .andExpect(status().isNotFound());
    }

    // ---------------- POST /api/tasks ----------------
    @Test
    void createTask_validRequest_createsTask() throws Exception {
        Task newTask = Task.builder()
                           .title("New Task")
                           .description("New Desc")
                           .status(TaskStatus.NEW)
                           .priority(TaskPriority.LOW)
                           .build();

        mockMvc.perform(post("/api/tasks")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(newTask)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.data.title", is("New Task")));
    }

    // ---------------- PATCH /api/tasks/{id} ----------------
    @Test
    void patchTask_existing_updatesTask() throws Exception {
        task1.setTitle("Updated Title");

        mockMvc.perform(patch("/api/tasks/{id}", task1.getId())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(task1)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data.title", is("Updated Title")));
    }

    @Test
    void patchTask_nonExisting_returnsNotFound() throws Exception {
        task1.setTitle("Updated Title");

        mockMvc.perform(patch("/api/tasks/{id}", 999)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(task1)))
               .andExpect(status().isNotFound());
    }

    // ---------------- DELETE /api/tasks/{id} ----------------
    @Test
    void deleteTask_existing_deletesTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", task1.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Task deleted successfully"))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteTask_nonExisting_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 999))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Task not found")))
               .andExpect(jsonPath("$.data").isEmpty());
    }
}

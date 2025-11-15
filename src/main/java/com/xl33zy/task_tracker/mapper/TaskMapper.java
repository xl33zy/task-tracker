package com.xl33zy.task_tracker.mapper;

import com.xl33zy.task_tracker.dto.TaskRequest;
import com.xl33zy.task_tracker.dto.TaskResponse;
import com.xl33zy.task_tracker.dto.TaskUpdateRequest;
import com.xl33zy.task_tracker.model.Task;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskRequest request);

    TaskResponse toResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromRequest(TaskUpdateRequest request, @MappingTarget Task task);
}

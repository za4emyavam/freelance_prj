package com.example.freelance.repositories;

import com.example.freelance.entities.RemovingReason;
import com.example.freelance.entities.RespondedContractor;
import com.example.freelance.entities.Task;
import com.example.freelance.entities.TaskFile;
import com.example.freelance.entities.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<Task> read(Long taskId);
    Task save(CreateTaskDTO task);
    Task update(Task task);
    void cancelTaskById(Long taskId);
    List<Task> findAllByCustomerId(Long customerId);
    List<ActiveTaskDTO> findAllActiveByField(String fieldName);
    List<ActiveTaskDTO> findAllActiveByEndDate(LocalDate date);
    List<RespondedContractorDTO> getContractorsByTask(Long taskId);
    TaskFile saveTaskFile(Long taskId, String link);
    List<TaskFile> findTaskFileByTask(Long taskId);
    void acceptContractor(Long taskId, Long contractorId);
    Optional<RespondedContractor> getRespondedContractor(Long taskId, Long contractorId);
    RemovingReason rejectAcceptedContractor(Long respondedContractorId, String reason);
    void rateTaskByTaskId(Long taskId, Integer rate);
    Optional<ContractorTaskDTO> findContractorTaskById(Long taskId);
    RespondedContractor agreeToTaskByTaskIdAndContractorId(Long taskId, Long contractId);
    List<ContractorTaskDTO> findAllContractorTasksByStatus(Long userId, Task.Status status);

}

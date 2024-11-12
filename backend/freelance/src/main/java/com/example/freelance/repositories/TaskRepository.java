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
    List<Task> findAllByCustomerIdWithOrder(Long customerId, String orderBy, String order, int offset);
    int findAllByCustomerIdTotalCount(Long customerId);
    List<ActiveTaskDTO> findAllActive(String orderBy, String ordering, int offset);
    int findAllActiveCount();
    List<ActiveTaskDTO> findAllActiveByField(String fieldName, String orderBy, String ordering, int offset);
    int findAllActiveByFieldCount(String fieldName);;
    List<ActiveTaskDTO> findAllContractorsTasksByStatus(Long contractorId, String status, String orderBy, String ordering, int offset);
    int findAllContractorsTasksCountByStatus(Long contractorId, String status);
    List<ActiveTaskDTO> findAllContractorsRejectedTasks(Long contractorId, String orderBy, String ordering, int offset);
    int findAllContractorsRejectedTasksCount(Long contractorId);
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
    Optional<RemovingReason> findRemovingReasonByContractorId(Long respondedContractor);

}

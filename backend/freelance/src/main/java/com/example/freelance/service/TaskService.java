package com.example.freelance.service;

import com.example.freelance.entities.*;
import com.example.freelance.entities.dto.*;
import com.example.freelance.exceptions.AccessDenied;
import com.example.freelance.exceptions.FieldOfActivityNotFound;
import com.example.freelance.exceptions.TaskNotFound;
import com.example.freelance.exceptions.ContractorNotFound;
import com.example.freelance.repositories.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final FieldOfActivityService fieldOfActivityService;

    public TaskService(TaskRepository taskRepository, FieldOfActivityService fieldOfActivityService) {
        this.taskRepository = taskRepository;
        this.fieldOfActivityService = fieldOfActivityService;
    }

    public Task save(CreateTaskDTO task) {
        if (fieldOfActivityService.getById(task.getIdActivityField()) == null)
            throw new FieldOfActivityNotFound("FieldOfActivity with id " + task.getIdActivityField() + " does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        task.setIdCustomer(((User) authentication.getPrincipal()).getIdUser());

        return taskRepository.save(task);
    }

    public CustomerTaskResponseDTO getAllTasksForCustomer(String orderBy, String ordering, int offset) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = ((User) authentication.getPrincipal()).getIdUser();

        if (ordering == null || !ordering.equals("desc"))
            ordering = "asc";

        if (offset < 0)
            offset = 0;

        if (!orderBy.equals("id_task") && !orderBy.equals("cost") && !orderBy.equals("end_date") &&
                !orderBy.equals("creation_date")) {
            ordering = "id_task";
        }

        List<Task> tasks = taskRepository.findAllByCustomerIdWithOrder(customerId, orderBy, ordering, offset);
        List<CustomerTaskDTO> dtos = new ArrayList<>();
        for (Task task : tasks) {
            /*List<RespondedContractorDTO> contractors = taskRepository.getContractorsByTask(task.getIdTask());*/
            FieldOfActivity fieldOfActivity = fieldOfActivityService.getById(task.getIdActivityField());
            dtos.add(new CustomerTaskDTO(task, fieldOfActivity, null));
        }

        return new CustomerTaskResponseDTO(dtos, taskRepository.findAllByCustomerIdTotalCount(customerId));
    }

    public List<RespondedContractorDTO> getAllContractorsByTask(Long taskId) {
        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(task.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");
        return taskRepository.getContractorsByTask(taskId);
    }

    public Task acceptContractor(Long taskId, Long contractorId) {
        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(task.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");

        getAllContractorsByTask(taskId).stream().findFirst().orElseThrow(
                () -> new ContractorNotFound("Contractor with id " + contractorId + " does not exist")
        );

        taskRepository.acceptContractor(taskId, contractorId);

        return taskRepository.read(taskId).get();
    }

    public RemovingReason rejectAcceptedContractor(Long taskId, Long contractorId, String reason) {
        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(task.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");
        if (!task.getStatus().equals(Task.Status.PERFORMING)) {
            throw new TaskNotFound("Task with id " + taskId + " is not perfomed");
        }

        RespondedContractor contractor = taskRepository.getRespondedContractor(taskId, contractorId)
                .orElseThrow(() -> new ContractorNotFound("Contractor with id " + contractorId + " does not exist"));


        return taskRepository.rejectAcceptedContractor(contractor.getIdRespondedContractor(), reason);
    }

    public Task rateTaskByTaskId(Long taskId, Integer rateVal) {
        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(task.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");
        if (!task.getStatus().equals(Task.Status.PERFORMING)) {
            throw new TaskNotFound("Task with id " + taskId + " is not perfomed");
        }

        taskRepository.rateTaskByTaskId(taskId, rateVal);

        return taskRepository.read(taskId).get();
    }

    public Task update(Long taskId, UpdateTaskDTO task) {
        Task oldTask = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(oldTask.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");

        if (task.getName() != null)
            oldTask.setName(task.getName());

        if (task.getDescription() != null)
            oldTask.setDescription(task.getDescription());

        if (task.getEndDate() != null)
            oldTask.setEndDate(task.getEndDate());

        if (task.getCost() != null)
            oldTask.setCost(task.getCost());

        if (task.getIdActivityField() != null) {
            if (fieldOfActivityService.getById(task.getIdActivityField()) == null)
                throw new FieldOfActivityNotFound("FieldOfActivity with id " + task.getIdActivityField() + " does not exist");

            oldTask.setIdActivityField(task.getIdActivityField());
        }
        List<TaskFile> links = new ArrayList<>();
        for (String link : task.getLinks()) {
            TaskFile taskFile = new TaskFile();
            taskFile.setIdTask(taskId);
            taskFile.setLink(link);
            links.add(taskFile);
        }

        oldTask.setLinks(links);

        return taskRepository.update(oldTask);
    }

    public void cancelTaskById(Long taskId) {
        Task oldTask = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(oldTask.getIdCustomer(), ((User) authentication.getPrincipal()).getIdUser()))
            throw new AccessDenied("Access denied");

        taskRepository.cancelTaskById(taskId);
    }

    public ContractorTaskDTO getContractorTaskById(Long taskId) {
        ContractorTaskDTO task = taskRepository.findContractorTaskById(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " not found")
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<RespondedContractor> optionalRespondedContractor = taskRepository
                .getRespondedContractor(taskId, ((User) authentication.getPrincipal()).getIdUser());

        RespondedContractor contractor = optionalRespondedContractor.orElse(null);
        if (!task.getStatus().equals(Task.Status.ACTIVE) && contractor == null) {
            throw new ContractorNotFound("Contractor not found");
        }

        if (contractor != null) {
            task.setFeedbackDate(contractor.getFeedbackDate());
            task.setContractorStatus(contractor.getStatus());
            Optional<RemovingReason> reason =
                    taskRepository.findRemovingReasonByContractorId(contractor.getIdRespondedContractor());
            if (reason.isPresent()) {
                task.setReason(reason.get().getReason());
                task.setRecallDate(reason.get().getRecallDate());
            }
        }

        return task;
    }

    public RespondedContractor agreedToTask(Long taskId) {
        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " does not exist"));
        if (!task.getStatus().equals(Task.Status.ACTIVE)) {
            throw new TaskNotFound("Task with id " + taskId + " does not have ACTIVE status");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getIdUser();

        return taskRepository.agreeToTaskByTaskIdAndContractorId(taskId, userId);
    }

    public List<ContractorTaskDTO> getAllContractorTasks(Task.Status status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getIdUser();

        return taskRepository.findAllContractorTasksByStatus(userId, status);
    }

    public ActiveTaskResponseDTO activeTasksWithSort(String sortBy, String value, String orderBy, String ordering, int offset) {
        if (ordering == null || !ordering.equals("desc"))
            ordering = "asc";

        if (offset <= 0)
            offset = 0;

        if (!orderBy.equals("id_task") && !orderBy.equals("cost") && !orderBy.equals("end_date") &&
                !orderBy.equals("creation_date")) {
            ordering = "id_task";
        }

        List<ActiveTaskDTO> tasks;

        if (sortBy != null && sortBy.equals("field")) {
            FieldOfActivity fieldOfActivity = fieldOfActivityService.getByName(value);
            if (!fieldOfActivity.getIsActive())
                throw new FieldOfActivityNotFound("Field of activity with name \"" + value + "\" is not active");

            tasks = taskRepository.findAllActiveByField(value, orderBy, ordering, offset);
            return new ActiveTaskResponseDTO(tasks, taskRepository.findAllActiveByFieldCount(value));
        } else {
            tasks = taskRepository.findAllActive(orderBy, ordering, offset);
            int totalCount = taskRepository.findAllActiveCount();
            return new ActiveTaskResponseDTO(tasks, totalCount);
        }
    }

    public ActiveTaskResponseDTO contractorsTasksWithSort(String sort, String value, String orderBy, String ordering, int offset) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getIdUser();

        if (ordering == null || !ordering.equals("desc"))
            ordering = "asc";

        if (offset <= 0)
            offset = 0;

        if (!orderBy.equals("id_task") && !orderBy.equals("cost") && !orderBy.equals("end_date") &&
                !orderBy.equals("creation_date")) {
            ordering = "id_task";
        }

        List<ActiveTaskDTO> tasks;
        if (sort != null && sort.equals("status") && value != null) {
            if (value.equals("PERFORMING") || value.equals("DONE")) {
                tasks = taskRepository.findAllContractorsTasksByStatus(userId, value, orderBy, ordering, offset);
                return new ActiveTaskResponseDTO(tasks, taskRepository.findAllContractorsTasksCountByStatus(userId, value));
            }
            if (value.equals("REJECTED")) {
                tasks = taskRepository.findAllContractorsRejectedTasks(userId, orderBy, ordering, offset);
                return new ActiveTaskResponseDTO(tasks, taskRepository.findAllContractorsRejectedTasksCount(userId));
            }
        }

        return null;
    }

    public CustomerTaskDTO getCustomerTaskByTaskId(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = ((User) authentication.getPrincipal()).getIdUser();

        Task task = taskRepository.read(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id: " + taskId + " not found"));

        if (!Objects.equals(task.getIdCustomer(), customerId))
            throw new AccessDenied("Access to task with id: " + taskId + " denied");

        List<RespondedContractorDTO> contractors = taskRepository.getContractorsByTask(task.getIdTask());
        FieldOfActivity fieldOfActivity = fieldOfActivityService.getById(task.getIdActivityField());

        return new CustomerTaskDTO(task, fieldOfActivity, contractors);
    }
}

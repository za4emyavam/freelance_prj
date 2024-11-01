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

    public List<CustomerTaskDTO> getAllTasksForCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = ((User) authentication.getPrincipal()).getIdUser();

        List<Task> tasks = taskRepository.findAllByCustomerId(customerId);
        List<CustomerTaskDTO> dtos = new ArrayList<>();
        for (Task task : tasks) {
            List<RespondedContractorDTO> contractors = taskRepository.getContractorsByTask(task.getIdTask());
            FieldOfActivity fieldOfActivity = fieldOfActivityService.getById(task.getIdActivityField());
            dtos.add(new CustomerTaskDTO(task, fieldOfActivity, contractors));
        }

        return dtos;
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
        if (!task.getStatus().equals(Task.Status.PERFORMED)) {
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
        if (!task.getStatus().equals(Task.Status.PERFORMED)) {
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

    public List<ActiveTaskDTO> activeTasks(String fieldName) {
        //Check if present
        FieldOfActivity fieldOfActivity = fieldOfActivityService.getByName(fieldName);
        if (!fieldOfActivity.getIsActive())
            throw new FieldOfActivityNotFound("Field of activity with name \"" + fieldName +"\" is not active");

        return taskRepository.findAllActiveByField(fieldName);
    }

    public ContractorTaskDTO getContractorTaskById(Long taskId) {
        return taskRepository.findContractorTaskById(taskId).orElseThrow(
                () -> new TaskNotFound("Task with id " + taskId + " not found")
        );
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

    public List<ActiveTaskDTO> activeTasksWithSort(String sortBy, String value) {
        switch (sortBy) {
            case "field":
                FieldOfActivity fieldOfActivity = fieldOfActivityService.getByName(value);
                if (!fieldOfActivity.getIsActive())
                    throw new FieldOfActivityNotFound("Field of activity with name \"" + value +"\" is not active");

                return taskRepository.findAllActiveByField(value);
            case "end_date":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                LocalDate endDate;
                try {
                    endDate = LocalDate.parse(value, formatter);
                } catch (DateTimeParseException exception) {
                    throw new TaskNotFound("Problem with parsing the date: " + value);
                }
                return taskRepository.findAllActiveByEndDate(endDate);
            default:
                return null;
        }
    }
}

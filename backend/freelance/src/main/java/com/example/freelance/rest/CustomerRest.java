package com.example.freelance.rest;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.entities.RemovingReason;
import com.example.freelance.entities.Task;
import com.example.freelance.entities.dto.*;
import com.example.freelance.service.ContractorService;
import com.example.freelance.service.FieldOfActivityService;
import com.example.freelance.service.TaskService;
import com.example.freelance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/customer")
public class CustomerRest {
    private final TaskService taskService;
    private final UserService userService;
    private final ContractorService contractorService;
    private final FieldOfActivityService fieldOfActivityService;

    public CustomerRest(TaskService taskService, UserService userService, ContractorService contractorService,
                        FieldOfActivityService fieldOfActivityService) {
        this.taskService = taskService;
        this.userService = userService;
        this.contractorService = contractorService;
        this.fieldOfActivityService = fieldOfActivityService;
    }

    @GetMapping("/user_info")
    public UserInfoDTO getUserInfo() {
        return userService.getUserInfo();
    }

    @PutMapping(value = "/user_info", consumes = "application/json")
    public UserInfoDTO updateUserInfo(@RequestBody UpdateUserDTO user) {
        return userService.update(user);
    }

    @PostMapping(value = "/tasks", consumes = "application/json")
    public Task saveTask(@RequestBody CreateTaskDTO task) {
        return taskService.save(task);
    }

    @GetMapping("/tasks/{taskId}/contractors")
    public List<RespondedContractorDTO> getAllContractorsByTask(@PathVariable Long taskId) {
        return taskService.getAllContractorsByTask(taskId);
    }

    @PostMapping("/tasks/{taskId}/contractors/{contractorId}")
    public Task acceptContractor(@PathVariable Long taskId,@PathVariable Long contractorId) {
        return taskService.acceptContractor(taskId, contractorId);
    }

    @PostMapping("/tasks/{taskId}/contractors/{contractorId}/reject")
    public RemovingReason rejectAcceptedContractor(@PathVariable Long taskId, @PathVariable Long contractorId,
                                                   @RequestBody String reason) {
        return taskService.rejectAcceptedContractor(taskId, contractorId, reason);
    }

    @PostMapping("/tasks/{taskId}/rate/{value}")
    public ResponseEntity<Task> rateTask(@PathVariable Long taskId, @PathVariable Integer value) {
        if (value < 1 || value > 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(taskService.rateTaskByTaskId(taskId, value));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskDTO task,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Task savedTask = taskService.update(taskId, task);

        return savedTask != null ? ResponseEntity.ok(savedTask) : new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{taskId}")
    public void cancelTask(@PathVariable Long taskId) {
        taskService.cancelTaskById(taskId);
    }

    @GetMapping("/tasks")
    public CustomerTaskResponseDTO getCustomerTasks(@RequestParam(required = false, defaultValue = "id_task") String orderBy,
                                                    @RequestParam(required = false) String ordering,
                                                    @RequestParam(required = false, defaultValue = "0") int offset) {
        return taskService.getAllTasksForCustomer(orderBy, ordering, offset);
    }

    @GetMapping("/tasks/{taskId}")
    public CustomerTaskDTO getCustomerTask(@PathVariable Long taskId) {
        return taskService.getCustomerTaskByTaskId(taskId);
    }

    @GetMapping("/contractors/{id}")
    public ContractorProfileDTO getContractorById(@PathVariable Long id) {
        return contractorService.getContractorById(id);
    }

    @DeleteMapping("/profile")
    public void deactivateProfile() {
        userService.deactivateProfile();
    }

    @GetMapping("/fields")
    public List<FieldOfActivity> getAllFields() {
        return fieldOfActivityService.getAllActive();
    }
}

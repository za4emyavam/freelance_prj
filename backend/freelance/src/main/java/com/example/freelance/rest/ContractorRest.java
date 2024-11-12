package com.example.freelance.rest;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.entities.RespondedContractor;
import com.example.freelance.entities.Task;
import com.example.freelance.entities.dto.*;
import com.example.freelance.repositories.FieldOfActivityRepository;
import com.example.freelance.service.ContractorService;
import com.example.freelance.service.FieldOfActivityService;
import com.example.freelance.service.TaskService;
import com.example.freelance.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/contractor")
public class ContractorRest {
    private final UserService userService;
    private final ContractorService contractorService;
    private final TaskService taskService;
    private final FieldOfActivityService fieldOfActivityService;

    public ContractorRest(UserService userService, ContractorService contractorService, TaskService taskService,
                          FieldOfActivityService fieldOfActivityService) {
        this.userService = userService;
        this.contractorService = contractorService;
        this.taskService = taskService;
        this.fieldOfActivityService = fieldOfActivityService;
    }

    @DeleteMapping("/profile")
    public void deactivateProfile() {
        userService.deactivateProfile();
    }

    @GetMapping("/profile")
    public ContractorDTO getProfile() {
        return contractorService.getProfile();
    }

    @GetMapping("/tasks")
    public ActiveTaskResponseDTO getAllActiveTaskWithSort(@RequestParam(required = false) String sort,
                                                        @RequestParam(required = false, defaultValue = "id_task") String orderBy,
                                                        @RequestParam(required = false) String ordering,
                                                        @RequestParam(required = false, defaultValue = "0") int offset,
                                                        @RequestHeader(value = "Value", required = false) String value) {
        return taskService.activeTasksWithSort(sort, value, orderBy, ordering, offset);
    }

    @GetMapping("/tasks/{taskId}")
    public ContractorTaskDTO getTask(@PathVariable Long taskId) {
        return taskService.getContractorTaskById(taskId);
    }

    @PostMapping("/tasks/{taskId}")
    public RespondedContractor agreedToTask(@PathVariable Long taskId) {
        return taskService.agreedToTask(taskId);
    }

    @GetMapping("/tasks/contractor")
    public ActiveTaskResponseDTO getAllContractorTaskWithSort(@RequestParam(required = false) String sort,
                                                          @RequestParam(required = false, defaultValue = "id_task") String orderBy,
                                                          @RequestParam(required = false) String ordering,
                                                          @RequestParam(required = false, defaultValue = "0") int offset,
                                                          @RequestHeader(value = "Value", required = false) String value) {
        return taskService.contractorsTasksWithSort(sort, value, orderBy, ordering, offset);
    }

    @GetMapping("/fields")
    public List<FieldOfActivity> getAllFields() {
        return fieldOfActivityService.getAllActive();
    }

    @GetMapping("/tasks/finished")
    public List<ContractorTaskDTO> getAllFinishedTasks() {
        return taskService.getAllContractorTasks(Task.Status.DONE);
    }

    @GetMapping("/tasks/active")
    public List<ContractorTaskDTO> getAllActiveTasks() {
        return taskService.getAllContractorTasks(Task.Status.PERFORMING);
    }

    @PutMapping(value = "/user_info", consumes = "application/json")
    public UserInfoDTO updateUserInfo(@RequestBody UpdateUserDTO user) {
        return userService.update(user);
    }
}

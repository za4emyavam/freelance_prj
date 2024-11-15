package com.example.freelance.rest;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.entities.Task;
import com.example.freelance.entities.dto.PeriodRequestDTO;
import com.example.freelance.repositories.FieldOfActivityRepository;
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
@RequestMapping("/v1/admin")
public class AdminRest {
    private final FieldOfActivityService fieldOfActivityService;
    private final UserService userService;
    private final TaskService taskService;

    public AdminRest(FieldOfActivityService fieldOfActivityService, UserService userService, TaskService taskService) {
        this.fieldOfActivityService = fieldOfActivityService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/fields")
    public List<FieldOfActivity> getFieldOfActivities() {
        return fieldOfActivityService.getAll();
    }

    @GetMapping("/field/{id}")
    public FieldOfActivity getFieldOfActivity(@PathVariable Long id) {
        return fieldOfActivityService.getById(id);
    }

    @PostMapping("/fields")
    public FieldOfActivity createFieldOfActivity(@RequestBody FieldOfActivity fieldOfActivity) {
        return fieldOfActivityService.createFieldOfActivity(fieldOfActivity);
    }

    @PutMapping("/fields")
    public FieldOfActivity updateFieldOfActivity(@RequestBody FieldOfActivity fieldOfActivity) {
        return fieldOfActivityService.updateFieldOfActivity(fieldOfActivity);
    }

    @DeleteMapping("/fields/{id}")
    public FieldOfActivity deactivateField(@PathVariable Long id) {
        return fieldOfActivityService.deactivateFieldOfActivity(id);
    }

    @PostMapping("/users")
    public ResponseEntity<?> countRegisteredUsersForPeriod(@Valid @RequestBody PeriodRequestDTO period,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(userService.countRegisteredUsersForPeriod(period));
    }

    @PostMapping("/tasks/done")
    public ResponseEntity<?> countDoneTask(@Valid @RequestBody PeriodRequestDTO period,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(taskService.countTaskByStatus(period, Task.Status.DONE));
    }

    @PostMapping("/tasks/cancelled")
    public ResponseEntity<?> countCancelledTask(@Valid @RequestBody PeriodRequestDTO period,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(taskService.countTaskByStatus(period, Task.Status.CANCELLED));
    }

    @PostMapping("/tasks/not_done")
    public ResponseEntity<?> countNotDoneTasks(@Valid @RequestBody PeriodRequestDTO period,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(taskService.countNotDoneTasks(period));
    }

    @GetMapping("/avg_rating")
    public double averageRating() {
        return userService.getAverageRating();
    }
}

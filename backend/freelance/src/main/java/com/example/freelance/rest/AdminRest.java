package com.example.freelance.rest;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.repositories.FieldOfActivityRepository;
import com.example.freelance.service.FieldOfActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminRest {
    private final FieldOfActivityService fieldOfActivityService;

    public AdminRest(FieldOfActivityService fieldOfActivityService) {
        this.fieldOfActivityService = fieldOfActivityService;
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
}

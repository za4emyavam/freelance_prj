package com.example.freelance.service;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.exceptions.FieldOfActivityNotFound;
import com.example.freelance.repositories.FieldOfActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldOfActivityService {
    private final FieldOfActivityRepository fieldOfActivityRepository;

    public FieldOfActivityService(FieldOfActivityRepository fieldOfActivityRepository) {
        this.fieldOfActivityRepository = fieldOfActivityRepository;
    }

    public FieldOfActivity getById(Long fieldId) {
        return fieldOfActivityRepository.retrieveById(fieldId).orElseThrow(
                () -> new FieldOfActivityNotFound("Field with id " + fieldId + " not found")
        );
    }

    public FieldOfActivity getByName(String name) {
        return fieldOfActivityRepository.readByName(name).orElseThrow(
                () -> new FieldOfActivityNotFound("Field with name \"" + name + "\" not found")
        );
    }

    public List<FieldOfActivity> getAllActive() {
        return fieldOfActivityRepository.readAllActive();
    }

    public List<FieldOfActivity> getAll() {
        return fieldOfActivityRepository.readAll();
    }

    public FieldOfActivity createFieldOfActivity(FieldOfActivity fieldOfActivity) {
        return fieldOfActivityRepository.save(fieldOfActivity);
    }

    public FieldOfActivity updateFieldOfActivity(FieldOfActivity field) {
        FieldOfActivity oldField = fieldOfActivityRepository.retrieveById(field.getIdField()).orElseThrow(
                () -> new FieldOfActivityNotFound("Field with id " + field.getIdField() + " not found")
        );

        if (field.getField() != null)
            oldField.setField(field.getField());

        oldField.setIsActive(field.getIsActive());

        return fieldOfActivityRepository.update(oldField);
    }

    public FieldOfActivity deactivateFieldOfActivity(Long fieldId) {
        FieldOfActivity oldField = fieldOfActivityRepository.retrieveById(fieldId).orElseThrow(
                () -> new FieldOfActivityNotFound("Field with id " + fieldId + " not found")
        );

        oldField.setIsActive(false);

        return fieldOfActivityRepository.update(oldField);
    }
}

package com.example.freelance.repositories;

import com.example.freelance.entities.FieldOfActivity;

import java.util.List;
import java.util.Optional;

public interface FieldOfActivityRepository {
    List<FieldOfActivity> readAll();
    List<FieldOfActivity> readAllActive();
    FieldOfActivity save(FieldOfActivity fieldOfActivity);
    FieldOfActivity update(FieldOfActivity fieldOfActivity);
    FieldOfActivity delete(FieldOfActivity fieldOfActivity);
    Optional<FieldOfActivity> retrieveById(Long fieldId);
    Optional<FieldOfActivity> readByName(String name);
//    FieldOfActivity readById(Long id);

}

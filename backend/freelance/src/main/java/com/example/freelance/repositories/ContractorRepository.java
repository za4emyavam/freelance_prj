package com.example.freelance.repositories;

import com.example.freelance.entities.dto.ContractorDTO;
import com.example.freelance.entities.dto.ContractorProfileDTO;

import java.util.Optional;

public interface ContractorRepository {
    Optional<ContractorProfileDTO> findContractorById(Long id);
    Optional<ContractorDTO> findContractorDTOById(Long id);
}

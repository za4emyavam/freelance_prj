package com.example.freelance.service;

import com.example.freelance.entities.User;
import com.example.freelance.entities.dto.ContractorDTO;
import com.example.freelance.entities.dto.ContractorProfileDTO;
import com.example.freelance.exceptions.ContractorNotFound;
import com.example.freelance.repositories.ContractorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContractorService {
    private final ContractorRepository contractorRepository;

    public ContractorService(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    public ContractorProfileDTO getContractorById(Long id) {
        return contractorRepository.findContractorById(id).orElseThrow(
                () -> new ContractorNotFound("Contractor with id " + id + " not found")
        );
    }

    public ContractorDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getIdUser();
        return contractorRepository.findContractorDTOById(userId).orElseThrow(
                () -> new ContractorNotFound("Contractor with id " + userId + " not found")
        );
    }


}

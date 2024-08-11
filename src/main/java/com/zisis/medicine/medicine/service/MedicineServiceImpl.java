package com.zisis.medicine.medicine.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.request.MedicineSearchRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import com.zisis.medicine.medicine.entity.Medicine;
import com.zisis.medicine.medicine.fhir.medicinalproductinteraction.MedicinalProductInteractionService;
import com.zisis.medicine.medicine.persistence.IMedicineRepository;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicinalProductInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements IMedicineService {

    private final IMedicineRepository medicineRepository;

    private final IGenericClient fhirClient;

    private final MedicinalProductInteractionService medicinalProductInteractionService;

    @Autowired
    public MedicineServiceImpl(IMedicineRepository medicineRepository, IGenericClient fhirClient, MedicinalProductInteractionService medicinalProductInteractionService) {
        this.medicineRepository = medicineRepository;
        this.medicinalProductInteractionService = medicinalProductInteractionService;
        this.fhirClient = fhirClient;
    }

    @Override
    public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {

        List<String> interactionWarnings = medicinalProductInteractionService.checkForDrugInteractions(medicineRepository.findAll(), Medicine.fromRequestDTO(request));

        if (!interactionWarnings.isEmpty()) {
            // Handle the interaction warning appropriately, e.g., log or notify the user
            System.out.println("Drug interactions found: " + String.join(", ", interactionWarnings));
        }

        Medicine medicine = request.toMedicineEntity();
        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineResponseDTO.fromEntity(savedMedicine);
    }

    @Override
    public MedicineResponseDTO getMedicine(Long id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        return medicineOptional
                .map(MedicineResponseDTO::fromEntity)
                .orElse(null);
    }

    @Override
    public MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO request) {
        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid medicine ID"));

        existingMedicine.setIngredient(request.getIngredient());
        existingMedicine.setExpiryDate(request.getExpiryDate());
        existingMedicine.setQuantity(request.getQuantity());

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);
        return MedicineResponseDTO.fromEntity(updatedMedicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        medicineRepository
                .delete(medicineRepository.getReferenceById(id));
    }

    @Override
    public List<MedicineResponseDTO> listAllMedicines() {
        return medicineRepository
                .findAll()
                .stream()
                .map(MedicineResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public List<MedicineResponseDTO> search(MedicineSearchRequestDTO dto) {
        return medicineRepository
                .findAllByIngredientOrManufacturer(dto.getIngredient(), dto.getManufacturer())
                .stream()
                .map(MedicineResponseDTO::fromEntity)
                .toList();
    }

}

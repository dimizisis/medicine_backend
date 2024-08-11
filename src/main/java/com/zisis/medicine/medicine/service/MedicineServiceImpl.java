package com.zisis.medicine.medicine.service;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.request.MedicineSearchRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import com.zisis.medicine.medicine.entity.Medicine;
import com.zisis.medicine.medicine.fhir.medication.MedicationService;
import com.zisis.medicine.medicine.fhir.medicinalproductinteraction.MedicinalProductInteractionService;
import com.zisis.medicine.medicine.persistence.IMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImpl implements IMedicineService {

    private final IMedicineRepository medicineRepository;
    private final MedicinalProductInteractionService medicinalProductInteractionService;
    private final MedicationService medicationService;

    @Autowired
    public MedicineServiceImpl(IMedicineRepository medicineRepository, MedicationService medicationService, MedicinalProductInteractionService medicinalProductInteractionService) {
        this.medicineRepository = medicineRepository;
        this.medicinalProductInteractionService = medicinalProductInteractionService;
        this.medicationService = medicationService;
    }

    @Override
    public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {

        List<String> interactionWarnings = medicinalProductInteractionService.checkForDrugInteractions(medicineRepository.findAll(), Medicine.fromRequestDTO(request));

        if (!interactionWarnings.isEmpty()) {
            // Handle the interaction warning, logging for now
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

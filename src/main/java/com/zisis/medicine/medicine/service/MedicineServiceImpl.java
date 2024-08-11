package com.zisis.medicine.medicine.service;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import com.zisis.medicine.medicine.entity.Medicine;
import com.zisis.medicine.medicine.persistence.IMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImpl implements IMedicineService {

    IMedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(IMedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {
        Medicine medicine = convertToEntity(request);
        Medicine savedMedicine = medicineRepository.save(medicine);
        return convertToResponse(savedMedicine);
    }

    @Override
    public MedicineResponseDTO getMedicine(Long id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        return medicineOptional
                .map(this::convertToResponse)
                .orElse(null);
    }

    @Override
    public MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO request) {
        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid medicine ID"));

        existingMedicine.setName(request.getName());
        existingMedicine.setExpiryDate(request.getExpiryDate());
        existingMedicine.setQuantity(request.getQuantity());

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);
        return convertToResponse(updatedMedicine);
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
                .map(this::convertToResponse)
                .toList();
    }

    private Medicine convertToEntity(MedicineRequestDTO dto) {
        return new Medicine(null,
                dto.getName(),
                dto.getExpiryDate(),
                dto.getQuantity());
    }

    private MedicineResponseDTO convertToResponse(Medicine entity) {
        return new MedicineResponseDTO(entity.getId(),
                entity.getName(),
                entity.getExpiryDate(),
                entity.getQuantity());
    }
}

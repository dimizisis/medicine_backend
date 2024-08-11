package com.zisis.medicine.medicine.service;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;

import java.util.List;

public interface IMedicineService {
    MedicineResponseDTO addMedicine(MedicineRequestDTO request);

    MedicineResponseDTO getMedicine(Long id);

    MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO request);

    void deleteMedicine(Long id);

    List<MedicineResponseDTO> listAllMedicines();
}

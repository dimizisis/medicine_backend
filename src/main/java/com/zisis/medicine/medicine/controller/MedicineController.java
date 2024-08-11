package com.zisis.medicine.medicine.controller;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import com.zisis.medicine.medicine.service.IMedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/medicines")
public class MedicineController {

    private IMedicineService medicineService;

    @Autowired
    public MedicineController(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<MedicineResponseDTO> addMedicine(@Valid @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO response = medicineService.addMedicine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> getMedicine(@PathVariable Long id) {
        MedicineResponseDTO response = medicineService.getMedicine(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> updateMedicine(@PathVariable Long id, @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO response = medicineService.updateMedicine(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponseDTO>> listMedicines() {
        List<MedicineResponseDTO> medicines = medicineService.listAllMedicines();
        return ResponseEntity.ok(medicines);
    }

}

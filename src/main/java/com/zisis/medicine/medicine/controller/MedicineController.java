package com.zisis.medicine.medicine.controller;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.request.MedicineSearchRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import com.zisis.medicine.medicine.service.IMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/api/medicines")
public class MedicineController {

    private final IMedicineService medicineService;

    @Autowired
    public MedicineController(IMedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<MedicineResponseDTO> add(@RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO response = medicineService.addMedicine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> get(@PathVariable Long id) {
        MedicineResponseDTO response = medicineService.getMedicine(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> update(@PathVariable Long id, @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO response = medicineService.updateMedicine(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<MedicineResponseDTO>> list() {
        List<MedicineResponseDTO> medicines = medicineService.listAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<MedicineResponseDTO>> search(@RequestBody MedicineSearchRequestDTO request) {
        List<MedicineResponseDTO> response = medicineService.search(request);
        return ResponseEntity.ok(response);
    }

}

package com.zisis.medicine.medicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicineResponseDTO {
    private Long id;
    private String name;
    private LocalDate expiryDate;
    private int quantity;
}

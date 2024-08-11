package com.zisis.medicine.medicine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineRequestDTO {
    private String name;
    private LocalDate expiryDate;
    private int quantity;
}

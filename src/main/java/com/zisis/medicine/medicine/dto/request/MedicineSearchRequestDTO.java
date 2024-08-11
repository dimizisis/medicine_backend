package com.zisis.medicine.medicine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineSearchRequestDTO {
    private String name;
    private String ingredient;
    private String manufacturer;
}

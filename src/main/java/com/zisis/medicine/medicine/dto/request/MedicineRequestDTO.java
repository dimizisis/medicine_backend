package com.zisis.medicine.medicine.dto.request;

import com.zisis.medicine.medicine.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineRequestDTO {
    private String name;
    private String ingredient;
    private String manufacturer;
    private Date expiryDate;
    private int quantity;

    public Medicine toMedicineEntity() {
        return new Medicine(null, name, ingredient, manufacturer, expiryDate, quantity);
    }
}

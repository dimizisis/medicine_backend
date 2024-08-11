package com.zisis.medicine.medicine.dto.response;

import com.zisis.medicine.medicine.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicineResponseDTO {
    private Long id;
    private String name;
    private String ingredient;
    private String manufacturer;
    private Date expiryDate;
    private int quantity;

    public Medicine toMedicineEntity() {
        return new Medicine(id, name, ingredient, manufacturer, expiryDate, quantity);
    }

    public static MedicineResponseDTO fromEntity(Medicine entity) {
        return new MedicineResponseDTO(entity.getId(), entity.getName(), entity.getIngredient(), entity.getManufacturer(), entity.getExpiryDate(), entity.getQuantity());
    }
}

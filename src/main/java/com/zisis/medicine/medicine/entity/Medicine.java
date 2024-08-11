package com.zisis.medicine.medicine.entity;

import com.zisis.medicine.medicine.dto.request.MedicineRequestDTO;
import com.zisis.medicine.medicine.dto.response.MedicineResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mid")
    private Long id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "ingredient")
    private String ingredient;

    @Basic(optional = false)
    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "quantity")
    private Integer quantity;

    public static Medicine fromResponseDTO(MedicineResponseDTO dto) {
        return new Medicine(dto.getId(),
                dto.getName(),
                dto.getIngredient(),
                dto.getManufacturer(),
                dto.getExpiryDate(),
                dto.getQuantity());
    }

    public static Medicine fromRequestDTO(MedicineRequestDTO dto) {
        return new Medicine(null,
                dto.getName(),
                dto.getIngredient(),
                dto.getManufacturer(),
                dto.getExpiryDate(),
                dto.getQuantity());
    }
}

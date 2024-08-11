package com.zisis.medicine.medicine.persistence;

import com.zisis.medicine.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMedicineRepository extends JpaRepository<Medicine, Long> {
    @Query(value = "SELECT * FROM Medicine WHERE ingredient LIKE %?1% " +
            "UNION ALL " +
            "SELECT * FROM Medicine WHERE manufacturer LIKE %?2%", nativeQuery = true)
    List<Medicine> findAllByIngredientOrManufacturer(String ingredient, String manufacturer);
}

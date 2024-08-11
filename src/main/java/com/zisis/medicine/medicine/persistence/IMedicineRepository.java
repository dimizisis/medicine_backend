package com.zisis.medicine.medicine.persistence;

import com.zisis.medicine.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicineRepository extends JpaRepository<Medicine, Long> {

}

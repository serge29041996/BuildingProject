package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByName(String name);
}

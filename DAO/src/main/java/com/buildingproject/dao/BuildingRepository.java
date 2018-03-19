package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
  Building findByName(String name);

  Building findById(long id);
}

package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
  Building findById(long id);

  Building findByName(String name);

  Building findByAddress(String address);

  Building findByNameAndAddress(String name, String address);
}

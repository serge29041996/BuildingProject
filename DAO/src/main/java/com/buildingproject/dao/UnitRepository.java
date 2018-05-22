package com.buildingproject.dao;

import com.buildingproject.commons.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{
  Unit findById(int id);

  Unit findByNumberAndIdBuilding(int number, long buildingId);

  List<Unit> findByIdBuilding(int buildingId);
}

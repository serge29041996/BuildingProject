package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{
  Unit findById(long id);

  Unit findByNumberAndBuilding(int number, Building building);

  List<Unit> findByBuilding(Building building);
}

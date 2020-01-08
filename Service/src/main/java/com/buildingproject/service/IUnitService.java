package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;

import java.util.List;

public interface IUnitService {
  Unit findById(long id);

  List<Unit> findUnitsByBuilding(Building building);

  Unit saveUnit(Unit unit, Building building);

  void updateUnit(Unit newUnit, Unit oldUnit);

  void deleteUnit(Unit unit);

  void deleteAll();

  boolean isUnitExistInBuilding(Unit unit, Building building);
}

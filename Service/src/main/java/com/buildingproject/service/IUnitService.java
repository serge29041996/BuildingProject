package com.buildingproject.service;

import com.buildingproject.commons.Unit;

public interface IUnitService {
  Unit findById(long id);

  Unit saveUnit(Unit unit);

  void updateUnit(Unit newUnit, Unit oldUnit);

  void deleteUnitById(long id);

  void deleteAll();

  boolean isUnitExistInBuilding(Unit unit);
}

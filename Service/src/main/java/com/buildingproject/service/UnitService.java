package com.buildingproject.service;

import com.buildingproject.commons.Unit;
import com.buildingproject.dao.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitService implements IUnitService{
  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private IBuildingService buildingService;

  @Override
  public Unit findById(long id) {
    return unitRepository.findById(id);
  }

  @Override
  public Unit saveUnit(Unit unit) {
    buildingService.addUnitToTheBuilding(unit.getBuilding());
    return unitRepository.save(unit);
  }

  @Override
  public void updateUnit(Unit newUnit, Unit oldUnit) {
    oldUnit.setNumber(newUnit.getNumber());
    oldUnit.setNumberBedrooms(newUnit.getNumberBedrooms());
    oldUnit.setAreaApartment(newUnit.getAreaApartment());
    oldUnit.setFurnitureAvailable(newUnit.isFurnitureAvailable());
    oldUnit.setAnimalsAllowed(newUnit.isAnimalsAllowed());
    unitRepository.flush();
  }

  @Override
  public void deleteUnitById(long id) {
    if(id > 0) {
      Unit unit = findById(id);
      buildingService.deleteUnitFromTheBuilding(unit.getBuilding());
      unitRepository.delete(id);
    }
  }

  @Override
  public void deleteAll() {
    this.unitRepository.deleteAll();
  }

  @Override
  public boolean isUnitExistInBuilding(Unit unit) {
    // need think about equals for the unit
    return false;
  }
}

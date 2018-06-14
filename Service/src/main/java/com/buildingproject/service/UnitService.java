package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.dao.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
  public Unit saveUnit(Unit unit, Building currentBuilding) {
      unit.setBuilding(currentBuilding);
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
  public void deleteUnit(Unit unit) {
    buildingService.deleteUnitFromTheBuilding(unit.getBuilding());
    unitRepository.delete(unit.getId());
  }

  @Override
  public void deleteAll() {
    this.unitRepository.deleteAll();
  }

  @Override
  public boolean isUnitExistInBuilding(Unit unit, Building building) {
      return unitRepository.findByNumberAndBuilding(unit.getNumber(), building) != null;
  }

  @Override
  public List<Unit> findUnitsByBuilding(Building building) {
      return unitRepository.findByBuilding(building);
  }
}

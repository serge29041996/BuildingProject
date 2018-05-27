package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;

import java.util.List;

public interface IBuildingService {
  List<Building> findAll();

  Building findById(long id);

  void deleteAll();

  Building saveBuilding(Building building);

  Building findByName(String name);

  Building findByAddress(String address);

  boolean isBuildingExist(Building building);

  void updateBuilding(Building newBuilding, Building oldBuilding);

  void deleteBuildingById(long id);

  Building findByNameAndAddress(String name, String address);

  List<Unit> getAllUnitsOfBuilding(long id);

  void addUnitToTheBuilding(Building building);

  void deleteUnitFromTheBuilding(Building building);
}
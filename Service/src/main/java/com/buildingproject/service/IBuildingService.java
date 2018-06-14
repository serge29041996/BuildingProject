package com.buildingproject.service;

import com.buildingproject.commons.Building;

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

  void deleteBuilding(Building building);

  Building findByNameAndAddress(String name, String address);

  void addUnitToTheBuilding(Building building);

  void deleteUnitFromTheBuilding(Building building);
}
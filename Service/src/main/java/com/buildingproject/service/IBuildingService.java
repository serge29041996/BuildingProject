package com.buildingproject.service;

import com.buildingproject.commons.Building;

import java.util.List;

public interface IBuildingService {
  List<Building> findAll();

  Building findById(long id);

  Building find(long id);

  void deleteAll();

  Building saveBuilding(Building building);

  Building findByName(String name);

  Building findByAddress(String address);

  boolean isBuildingExist(Building building);

  void updateBuilding(Building building);

  void deleteBuildingById(long id);

  Building findByNameAndAddress(String name, String address);

  String validateDataBuilding(Building building);
}
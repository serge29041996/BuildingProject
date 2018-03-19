package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService implements IBuildingService {
  @Autowired
  private BuildingRepository buildingRepository;

  @Override
  public List<Building> findAll() {
    List<Building> buildings = (List<Building>) buildingRepository.findAll();
    return buildings;
  }


  @Override
  public void deleteAll() {
    buildingRepository.deleteAll();
  }

  @Override
  public Building saveBuilding(Building building) {
    return buildingRepository.save(building);
  }

  @Override
  public Building find(long id) {
    return new Building();
  }

  @Override
  public Building findById(long id) {
    Building building = buildingRepository.findById(id);
    if (building == null) {
      return null;
    } else {
      return building;
    }
  }

  @Override
  public Building findByName(String name) {
    Building building = buildingRepository.findByName(name);
    if (building == null) {
      return null;
    } else {
      return building;
    }
  }

  @Override
  public boolean isBuildingExist(Building building) {
    return findByName(building.getName()) != null;
  }

  @Override
  public void updateBuilding(Building building) {
    Building needUpdateBuilding = findById(building.getId());
    if (needUpdateBuilding != null) {
      needUpdateBuilding.setName(building.getName());
      needUpdateBuilding.setAddress(building.getAddress());
      buildingRepository.flush();
    }
  }

  @Override
  public void deleteBuildingById(long id) {
    Building needDeleteBuilding = findById(id);
    if (needDeleteBuilding != null) {
      buildingRepository.delete(needDeleteBuilding);
    }
  }
}
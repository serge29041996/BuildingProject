package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
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
    List<Building> buildings =  buildingRepository.findAll();
    for (Building building : buildings) {
      building.setNumberUnits(0);
    }
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
  public Building findById(long id) {
    if(id >= 0) {
      return buildingRepository.findById(id);
    } else {
      return null;
    }
  }

  @Override
  public Building findByName(String name) {
    return buildingRepository.findByName(name);
  }

  @Override
  public Building findByNameAndAddress(String name, String address) {
    return buildingRepository.findByNameAndAddress(name, address);
  }

  @Override
  public Building findByAddress(String address) {
    return buildingRepository.findByAddress(address);
  }

  @Override
  public boolean isBuildingExist(Building building) {
    return findByNameAndAddress(building.getName(), building.getAddress()) != null;
  }

  @Override
  public void updateBuilding(Building newBuilding, Building oldBuilding) {
    oldBuilding.setName(newBuilding.getName());
    oldBuilding.setAddress(newBuilding.getAddress());
    //qoldBuilding.setNumberUnits(newBuilding.getNumberUnits());
    oldBuilding.setNumberResidents(newBuilding.getNumberResidents());
    oldBuilding.setImage(newBuilding.getImage());
    oldBuilding.setTypeImage(newBuilding.getTypeImage());
    buildingRepository.flush();
  }

  @Override
  public void deleteBuildingById(long id) {
    Building needDeleteBuilding = findById(id);
    if (needDeleteBuilding != null) {
      buildingRepository.delete(needDeleteBuilding);
    }
  }

  @Override
  public List<Unit> getAllUnitsOfBuilding(long id) {
    Building building = findById(id);
    if(building != null) {
      return building.getUnits();
    } else {
      return null;
    }
  }

  @Override
  public void addUnitToTheBuilding(Building building) {
    int numberUnits = building.getNumberUnits();
    building.setNumberUnits(numberUnits + 1);
    buildingRepository.flush();
  }

  @Override
  public void deleteUnitFromTheBuilding(Building building) {
    int numberUnits = building.getNumberUnits();
    building.setNumberUnits(numberUnits - 1);
    buildingRepository.flush();
  }
}
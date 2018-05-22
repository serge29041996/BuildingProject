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
    return buildingRepository.findAll();
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
    return id >= 0 ? buildingRepository.findById(id) : null;
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
    oldBuilding.setNumberUnits(newBuilding.getNumberUnits());
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
}
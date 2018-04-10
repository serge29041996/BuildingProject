package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

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
  public String validateDataBuilding(Building building) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Building>> violations = validator.validate(building);
    if (violations.size() == 0) {
      return "";
    }
    else {
      StringBuilder errorMessage = new StringBuilder("");
      for(ConstraintViolation<Building> violation : violations){
        errorMessage.append(violation.getMessage());
        errorMessage.append(".");
      }
      return errorMessage.toString();
    }
  }

  @Override
  public void updateBuilding(Building building) {
    Building needUpdateBuilding = findById(building.getId());
    if (needUpdateBuilding != null) {
      needUpdateBuilding.setName(building.getName());
      needUpdateBuilding.setAddress(building.getAddress());
      needUpdateBuilding.setNumberUnits(building.getNumberUnits());
      needUpdateBuilding.setNumberResidents(building.getNumberResidents());
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
package com.buildingproject.service;

import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService implements IBuildingService{
  @Autowired
  private BuildingRepository buildingRepository;

  @Override
  public List<Building> findAll() {
    List<Building> buildings = (List<Building>) buildingRepository.findAll();
    return buildings;
  }
}
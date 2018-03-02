package com.buildingproject.service;

import com.buildingproject.commons.Building;

import java.util.List;

public interface IBuildingService {
  List<Building> findAll();
}
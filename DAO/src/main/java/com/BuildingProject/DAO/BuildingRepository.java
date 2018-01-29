package com.BuildingProject.DAO;

import com.BuildingProject.Commons.Building;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BuildingRepository extends CrudRepository<Building, Long> {
    List<Building> findByName(String name);
}

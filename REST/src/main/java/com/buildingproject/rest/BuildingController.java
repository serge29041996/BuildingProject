package com.buildingproject.rest;

import com.buildingproject.commons.Building;
import com.buildingproject.service.IBuildingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/")
@Api(value="building", description = "Operations pertaining to buildings")
public class BuildingController {
  public static final Logger logger = LoggerFactory.getLogger(RestSpringClass.class);

  @Autowired
  private IBuildingService buildingService;

  @ApiOperation(value = "View a list of available buildings", response = List.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved list"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping("/buildings")
  public List<Building> getAllBuildings() {
    return buildingService.findAll();
  }

  @ApiOperation(value = "Search a building with an ID")
  @GetMapping("/building/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id){
    logger.info("Fetching Building with id {}", id);
    Building building = buildingService.findById(id);
    if (building == null) {
      logger.error("Building with name {} not found.", id);
      return new ResponseEntity("Building with id " + id
          + " not found", HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Building>(building, HttpStatus.OK);
  }

  @ApiOperation(value = "Add a building")
  @PostMapping("/building")
  public ResponseEntity<?> createUser(@RequestBody Building building, UriComponentsBuilder ucBuilder) {
    logger.info("Creating Building : {}", building);

    if (buildingService.isBuildingExist(building)) {
      logger.error("Unable to create. A User with id {} already exist", building.getName());
      return new ResponseEntity("Unable to create. A User with id " +
          building.getName() + " already exist.",HttpStatus.CONFLICT);
    }
    buildingService.saveBuilding(building);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("building/{id}").buildAndExpand(building.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update building with an ID")
  @PutMapping("/building/{id}")
  public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody Building building) {
    logger.info("Updating User with id {}", id);

    Building currentBuilding = buildingService.findById(id);

    if (currentBuilding == null) {
      logger.error("Unable to update. User with id {} not found.", id);
      return new ResponseEntity("Unable to upate. User with id " + id + " not found.",
          HttpStatus.NOT_FOUND);
    }

    currentBuilding.setName(building.getName());
    currentBuilding.setAddress(building.getAddress());

    buildingService.updateBuilding(currentBuilding);
    return new ResponseEntity<Building>(currentBuilding, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete building with an ID")
  @DeleteMapping("/building/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    logger.info("Fetching & Deleting User with id {}", id);

    Building user = buildingService.findById(id);
    if (user == null) {
      logger.error("Unable to delete. User with id {} not found.", id);
      return new ResponseEntity("Unable to delete. User with id " + id + " not found.",
          HttpStatus.NOT_FOUND);
    }
    buildingService.deleteBuildingById(id);
    return new ResponseEntity<Building>(HttpStatus.OK);
  }
}

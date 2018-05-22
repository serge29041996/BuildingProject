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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
@Api(value="building", description = "Operations pertaining to buildings")
public class BuildingController {
  private static final Logger logger = LoggerFactory.getLogger(RestSpringClass.class);

  @Autowired
  private IBuildingService buildingService;

  @ApiOperation(value = "View a list of available buildings", response = List.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved list"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping("/buildings")
  @CrossOrigin(origins = "http://localhost:4200")
  public List<Building> getAllBuildings() {
    return buildingService.findAll();
  }

  @ApiOperation(value = "Search a building with an ID")
  @GetMapping("/building/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> getUser(@PathVariable long id){
    logger.info("Fetching Building with id {}", id);
    Building building = buildingService.findById(id);
    if (building == null) {
      logger.error("Building with name {} not found.", id);
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,String.format("Building with id %d not found", id));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    return new ResponseEntity<>(building, HttpStatus.OK);
  }

  @ApiOperation(value = "Add a building")
  @PostMapping("/building")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> createUser(@RequestBody @Valid Building building, UriComponentsBuilder ucBuilder) {
    logger.info("Creating Building : {}", building);

    if (buildingService.isBuildingExist(building)) {
      logger.error("Unable to create. A Building with name {} already exist", building.getName());
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, String.format("Unable to create building. A Building "
          + "with name %s and address %s already exist.", building.getName(), building.getAddress()));
      return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    building = buildingService.saveBuilding(building);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("building/{id}").buildAndExpand(building.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update building with an ID")
  @PutMapping("/building/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody @Valid Building building) {
    logger.info("Updating Building with id {}", id);

    Building currentBuilding = buildingService.findById(id);

    if (currentBuilding == null) {
      logger.error("Unable to update. Building with id {} not found.", id);
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, String.format("Unable to update building. Building"
          + " with %d not found.",id));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    buildingService.updateBuilding(building, currentBuilding);
    return new ResponseEntity<>(currentBuilding, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete building with an ID")
  @DeleteMapping("/building/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    logger.info("Fetching & Deleting User with id {}", id);

    Building user = buildingService.findById(id);
    if (user == null) {
      logger.error("Unable to delete. Building with id {} not found.", id);
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, String.format("Unable to delete building. Building with "
      + "id %d not found",id));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    buildingService.deleteBuildingById(id);
    return new ResponseEntity<Building>(HttpStatus.OK);
  }
}

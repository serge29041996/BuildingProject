package com.buildingproject.rest.controllers;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.rest.RestSpringClass;
import com.buildingproject.rest.exceptions.ApiError;
import com.buildingproject.service.IBuildingService;
import com.buildingproject.service.IUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@Api(value="unit", description = "Operations pertaining to unit")
public class UnitController {
  private static final Logger logger = LoggerFactory.getLogger(RestSpringClass.class);

  @Autowired
  private IUnitService unitService;

  @Autowired
  IBuildingService buildingService;

  @ApiOperation(value = "Add a unit to the building")
  @PostMapping("/unit/{buildingId}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> createUser(@PathVariable("buildingId") long buildingId,
                                      @RequestBody @Valid Unit unit, UriComponentsBuilder ucBuilder) {
    logger.info("Creating Unit : {}", unit);

    Building building = buildingService.findById(buildingId);

    if (building == null) {
      logger.error("Building with id {} not found.", buildingId);
      ApiError apiError = new ApiError(
          HttpStatus.NOT_FOUND,String.format("Building with id %d not found", buildingId));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    if (unitService.isUnitExistInBuilding(unit, building)) {
      logger.error("Unable to create. A Unit with number {} already exist", unit.getNumber());
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, String.format("Unable to create unit. A Unit "
          + "with number %d already exist.", unit.getNumber()));
      return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    unit = unitService.saveUnit(unit, building);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("unit/{id}").buildAndExpand(unit.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update unit with an ID")
  @PutMapping("/unit/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody @Valid Unit updatedUnit) {
    logger.info("Updating Unit with id {}", id);

    Unit currentUnit = unitService.findById(id);

    if (currentUnit == null) {
      logger.error("Unable to update. Unit with id {} not found.", id);
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, String.format("Unable to update unit. Unit"
          + " with %d not found.",id));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    unitService.updateUnit(updatedUnit, currentUnit);
    return new ResponseEntity<>(currentUnit, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete unit with an ID")
  @DeleteMapping("/unit/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    logger.info("Fetching & Deleting Unit with id {}", id);

    Unit unit = unitService.findById(id);
    if (unit == null) {
      logger.error("Unable to delete. Unit with id {} not found.", id);
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, String.format("Unable to delete unit. Unit with "
          + "id %d not found",id));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    unitService.deleteUnit(unit);
    return new ResponseEntity<Unit>(HttpStatus.OK);
  }

  @ApiOperation(value = "Get all units of building with ID")
  @DeleteMapping("units/{buildingId}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<?> getUnitsOfBuilding(@PathVariable("buildingId") long buildingId) {
    logger.info("Fetching Units from Building with id {}", buildingId);

    Building building = buildingService.findById(buildingId);

    if(building == null) {
      logger.error("Building with id {} not found.", buildingId);
      ApiError apiError = new ApiError(
          HttpStatus.NOT_FOUND,String.format("Building with id %d not found", buildingId));
      return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    unitService.findUnitsByBuilding(building);
    return new ResponseEntity<Unit>(HttpStatus.OK);
  }
}

package com.buildingproject.rest;

import com.buildingproject.commons.Building;
import com.buildingproject.service.IBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BuildingController {

  @Autowired
  private IBuildingService buildingService;

  @RequestMapping("/")
  public String index(Model model) {
    return "index";
  }

  @RequestMapping("/showBuildings")
  public ModelAndView showCities() {

    List<Building> cities = buildingService.findAll();

    Map<String, Object> params = new HashMap<>();
    params.put("cities", cities);

    return new ModelAndView("showCities", params);
  }

}

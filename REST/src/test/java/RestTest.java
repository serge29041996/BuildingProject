import com.buildingproject.commons.Building;
import com.buildingproject.service.IBuildingService;
import com.buildingproject.rest.RestSpringClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RestSpringClass.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class RestTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private IBuildingService buildingService;

  @Before
  public void deleteAllBuildings(){
    buildingService.deleteAll();
  }

  @Test
  public void testFindAllBuildings() throws Exception {
    createBuilding("example");

    mvc.perform(get("/buildings")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("example")));
  }

  @Test
  public void testGetBuilding() throws Exception {
    Building building = new Building("building_name", "building_address");
    Building savedBuilding = buildingService.saveBuilding(building);

    mvc.perform(get("/building/{id}", savedBuilding.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name", is("building_name")));
  }

  @Test
  public void testPostBuilding() throws Exception {
    Building building = new Building("building_name", "building_address");
    mvc.perform(
        post("/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(building)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", containsString("http://localhost/building/")));

    mvc.perform(
        get("/buildings")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("building_name")));
  }

  @Test
  public void testPutBuilding() throws Exception {
    Building building = new Building("building_name", "building_address");
    buildingService.saveBuilding(building);
    Building needBuilding = buildingService.findByName(building.getName());
    Building newBuilding = new Building("new_building_name", "new_building_address");

    mvc.perform(
        put("/building/{id}", needBuilding.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newBuilding)))
        .andExpect(status().isOk());

    mvc.perform(
        get("/building/{id}", needBuilding.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name", is("new_building_name")));
  }

  @Test
  public void testDeleteBuilding() throws Exception {
    Building building = new Building("building_name","building_address");
    buildingService.saveBuilding(building);
    Building needBuilding = buildingService.findByName(building.getName());

    mvc.perform(
        delete("/building/{id}",needBuilding.getId()))
        .andExpect(status().isOk());

    mvc.perform(
        get("/building/{id}", needBuilding.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  /*
  @Test
  public void testFindById (){
    Building building = buildingService.find(100L);
  }
  */

  private void createBuilding(String name) {
    Building building = new Building(name, name);
    buildingService.saveBuilding(building);
  }

  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
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


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;


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
    createBuilding("example1");
    createBuilding("example2");
    createBuilding("example3");

    mvc.perform(get("/buildings")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("example")))
        .andExpect(jsonPath("$[1].name", is("example1")))
        .andExpect(jsonPath("$[2].name", is("example2")))
        .andExpect(jsonPath("$[3].name", is("example3")));
  }

  @Test
  public void testGetBuilding() throws Exception {
    Building building = new Building("building_name", "building_address", 1, 1);
    Building savedBuilding = buildingService.saveBuilding(building);

    mvc.perform(get("/building/{id}", savedBuilding.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name", is("building_name")));
  }

  @Test
  public void testPostBuildingCorrectData() throws Exception {
    Building building = new Building("building_name", "building_address", 1, 1);

    MvcResult mvcResult =
        mvc.perform(
            post("/building")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(building)))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", containsString("http://localhost/building/")))
            .andReturn();

    long id = Long.parseLong(mvcResult.getResponse().getRedirectedUrl().split("/")[4]);

    mvc.perform(
        get("/building/{id}",id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().
            contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name", is("building_name")));

    mvc.perform(
        post("/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(building)))
        .andExpect(status().isConflict());
  }

  @Test
  public void testPutBuilding() throws Exception {
    Building building = new Building("building_name", "building_address", 1, 1);
    Building needBuilding = buildingService.saveBuilding(building);
    Building newBuilding = new Building("new_building_name", "new_building_address", 2, 2);

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
    Building building = new Building("building_name","building_address", 1, 1);
    Building needBuilding = buildingService.saveBuilding(building);

    mvc.perform(
        delete("/building/{id}",needBuilding.getId()))
        .andExpect(status().isOk());

    mvc.perform(
        get("/building/{id}", needBuilding.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testPostExistingBuilding() throws Exception {
    Building building = new Building("building_name","building_address",
        1, 1);

    mvc.perform(
        post("/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(building)))
        .andExpect(status().isCreated());

    MvcResult mvcResult =
    mvc.perform(
        post("/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(building)))
        .andExpect(status().isConflict())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    assertThat(requestBody).isEqualTo(
        String.format("Unable to create. A Building with name %s and address %s already exist.",
            building.getName(), building.getAddress()));
  }

  @Test
  public void testWrongDataPostBuilding() throws Exception {
    Building building = new Building("building_name","building_address",
        -1, -1);

    MvcResult mvcResult =
        mvc.perform(
            post("/building")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(building)))
            .andExpect(status().isConflict())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();

    assertThat(requestBody).isEqualTo(
        "Number of residents must be greater than 0 or equal 0."
            + "Number of the units must be greater than 0 or equal 0.");
  }

  @Test
  public void testWrongDataUpdate() throws Exception {
    Building building = new Building("building_name","building_address",
        10, 10);

    building = buildingService.saveBuilding(building);

    building.setName(null);

    MvcResult mvcResult =
    mvc.perform(
        put("/building/{id}", building.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(building)))
        .andExpect(status().isConflict())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();

    assertThat(requestBody).isEqualTo(
        "Name of the building cannot be null or whitespace.");
  }

  private void createBuilding(String name) {
    Building building = new Building(name, name, 1, 1);
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
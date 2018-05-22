import com.buildingproject.commons.Building;
import com.buildingproject.rest.ApiError;
import com.buildingproject.rest.ApiValidationError;
import com.buildingproject.rest.RestSpringClass;
import com.buildingproject.service.IBuildingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileReader;
import java.util.Base64;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        .andExpect(status().isBadRequest());
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
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiError(requestBody).getMessage();
    assertThat(message).isEqualTo(
        String.format("Unable to create building. A Building with name %s and address %s already exist.",
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
            .andExpect(status().isBadRequest())
            .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    ApiError apiError = asJavaObjectApiError(requestBody);
    StringBuilder messages = new StringBuilder("");
    for(ApiValidationError error: apiError.getSubErrors()){
      messages.append(error.getMessage());
      messages.append(".");
    }
    assertThat("Number of the units must be greater than 0 or equal 0.").isSubstringOf(messages.toString());
    assertThat("Number of residents must be greater than 0 or equal 0.").isSubstringOf(messages.toString());
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
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiError(requestBody).getSubErrors().get(0).getMessage();
    assertThat(message).isEqualTo(
        "Name of the building cannot be null or whitespace");
  }

  @Test
  public void testWritingDataWithBlobObject() throws Exception {
    FileReader fr = new FileReader("src/test/resources/base64String.txt");
    Scanner scanner = new Scanner(fr);

    String base64ImageInfo = scanner.nextLine();

    scanner.close();
    fr.close();

    byte[] decodedString = Base64.getDecoder().decode(base64ImageInfo);

    Building building = new Building("building_name", "building_address", 1,
        1, decodedString);

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
        .andExpect(jsonPath("image", is(base64ImageInfo)));
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

  public static ApiError asJavaObjectApiError(final String JSONResponse){
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(JSONResponse,ApiError.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.rest.ApiError;
import com.buildingproject.rest.ApiValidationError;
import com.buildingproject.rest.RestSpringClass;
import com.buildingproject.service.IBuildingService;
import com.buildingproject.service.IUnitService;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RestSpringClass.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class RestUnitTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private IUnitService unitService;

  @Autowired
  private IBuildingService buildingService;

  @Before
  public void deleteAllBuildings(){
    buildingService.deleteAll();
    unitService.deleteAll();
  }

  @Test
  public void testPostCorrectDataUnit() throws Exception {
    //If don`t save this building will obtain error
    //object references an unsaved transient instance - save the transient instance before flushing
    Building building = new Building("1", "1", 10);

    buildingService.saveBuilding(building);

    Unit unit = new Unit(1,2,60,true,
        true,building);

    MvcResult mvcResult = mvc.perform(
        post("/unit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", containsString("http://localhost/unit/")))
        .andReturn();

    long id = Long.parseLong(mvcResult.getResponse().getRedirectedUrl().split("/")[4]);

    Unit createdUnit = unitService.findById(id);

    //This two object will not be equal
    assertThat(createdUnit).isEqualTo(unit);
  }

  @Test
  public void testPostWrongDataUnit() throws Exception {
    Building building = new Building("2","2", 20);
    buildingService.saveBuilding(building);

    Unit unit = new Unit(-1,3,90,false,
        false,building);

    MvcResult mvcResult = mvc
        .perform(
            post("/unit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    ApiError apiError = asJavaObjectApiError(requestBody);
    StringBuilder messages = new StringBuilder("");
    for(ApiValidationError error: apiError.getSubErrors()){
      messages.append(error.getMessage());
      messages.append(".");
    }
    assertThat("Number of the unit must be greater than 0.").
        isSubstringOf(messages.toString());
  }

  @Test
  public void testPostExistingUnit() throws Exception {
    Building building = new Building("3", "3", 10);
    buildingService.saveBuilding(building);

    Unit unit = new Unit(10,2,60,true,
        true,building);

    mvc.perform(
        post("/unit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isCreated());

    MvcResult mvcResult = mvc.perform(
        post("/unit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiError(requestBody).getMessage();
    assertThat(message).isEqualTo(
        String.format("Unable to create unit. A Unit with number %d already exist.",
            unit.getNumber()));
  }

  @Test
  public void testPutCorrectDataUnit() throws Exception {
    Building building = new Building("4", "4", 20);
    buildingService.saveBuilding(building);
    Unit unit = new Unit(4,1,30,false,
        false,building);
    unit = unitService.saveUnit(unit);

    Unit newUnit = new Unit(5,1,30,false,
        false,building);

    mvc.perform(
        put("/unit/{id}", unit.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(newUnit)))
        .andExpect(status().isOk());

    Unit savedNewUnit = unitService.findById(unit.getId());

    assertThat(savedNewUnit.getNumber()).isEqualTo(newUnit.getNumber());
  }

  @Test
  public void testPutWrongDataUnit() throws Exception {
    Building building = new Building("5", "5", 15);
    buildingService.saveBuilding(building);
    Unit unit = new Unit(5,3,90,true,
        false,building);
    unit = unitService.saveUnit(unit);

    unit.setAreaApartment(0);

    MvcResult mvcResult = mvc.perform(
        put("/unit/{id}", unit.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isBadRequest())
        .andReturn();

    String requestBody = mvcResult.getResponse().getContentAsString();
    String message = asJavaObjectApiError(requestBody).getSubErrors().get(0).getMessage();
    assertThat(message).isEqualTo(
        "Area apartment must be greater than 0");
  }

  @Test
  public void testDeleteUnit() throws Exception {
    Building building = new Building("6", "6", 40);
    building = buildingService.saveBuilding(building);
    Unit unit = new Unit(1,4,120,true,
        true,building);
    unit = unitService.saveUnit(unit);

    mvc.perform(
        delete("/unit/{id}", unit.getId()))
        .andExpect(status().isOk());

    Unit needUnit = unitService.findById(unit.getId());

    assertThat(needUnit).isEqualTo(null);
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

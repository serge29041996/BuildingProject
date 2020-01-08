import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.rest.RestSpringClass;
import com.buildingproject.rest.exceptions.ApiError;
import com.buildingproject.rest.exceptions.ApiValidationError;
import com.buildingproject.service.IBuildingService;
import com.buildingproject.service.IUnitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.List;
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
    Building building = new Building("1", "1", 10);

    building = buildingService.saveBuilding(building);

    Unit unit = new Unit(1,2,60,true,true, null);

    MvcResult mvcResult = mvc.perform(
        post("/unit/{buildingId}", building.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", containsString("http://localhost/unit/")))
        .andReturn();

    unit.setBuilding(building);
    building.setNumberUnits(building.getNumberUnits() + 1);

    Building needBuilding = buildingService.findById(building.getId());

    long id = Long.parseLong(mvcResult.getResponse().getRedirectedUrl().split("/")[4]);

    Unit createdUnit = unitService.findById(id);

    assertThat(createdUnit).isEqualTo(unit);
    assertThat(needBuilding.getNumberUnits()).isEqualTo(1);
  }

  @Test
  public void testPostWrongDataUnit() throws Exception {
    Building building = new Building("2","2", 20);
    building = buildingService.saveBuilding(building);

    Unit unit = new Unit(-1,3,90,false,
        false,null);

    MvcResult mvcResult = mvc
        .perform(
            post("/unit/{buildingId}", building.getId())
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
    building = buildingService.saveBuilding(building);

    Unit unit = new Unit(10,2,60,true,
        true, null);

    mvc.perform(
        post("/unit/{buildingId}", building.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(unit)))
        .andExpect(status().isCreated());

    MvcResult mvcResult = mvc.perform(
        post("/unit/{buildingId}", building.getId())
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
    building = buildingService.saveBuilding(building);
    Unit unit = new Unit(4,1,30,false,
        false, null);
    unit = unitService.saveUnit(unit, building);

    Unit newUnit = new Unit(5,1,30,false,
        false, building);

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
    building = buildingService.saveBuilding(building);
    Unit unit = new Unit(5,3,90,true,
        false, null);
    unit = unitService.saveUnit(unit, building);

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
        true, null);
    unit = unitService.saveUnit(unit, building);
    // Building needBuilding1 = buildingService.findById(building.getId());
    // List<Building> buildingsList = buildingService.findAll();

    mvc.perform(
        delete("/unit/{id}", unit.getId()))
        .andExpect(status().isOk());

    Unit needUnit = unitService.findById(unit.getId());

    Building needBuilding = buildingService.findById(building.getId());

    assertThat(needUnit).isEqualTo(null);
    assertThat(needBuilding.getNumberUnits()).isEqualTo(0);
  }

  @Test
  public void testGettingUnitsOfBuilding() throws Exception {
    Building building = new Building("6", "6", 40);
    building = buildingService.saveBuilding(building);
    Unit unit = new Unit(1,4,120,true,
        true, null);
    unit = unitService.saveUnit(unit, building);
    unit = new Unit(2,3,90,true,
        true, null);
    unit = unitService.saveUnit(unit, building);
    unit = new Unit(3,1,30,false,
        false, null);
    unit = unitService.saveUnit(unit, building);

    MvcResult mvcResult = mvc.perform(
        get("/units/{buildingId}", building.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    List<Unit> readUnits = asListUnits(response);
    assertThat(readUnits.size()).isEqualTo(3);
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

  public static List<Unit> asListUnits(final String JSONResponse) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      TypeFactory typeFactory = objectMapper.getTypeFactory();
      return objectMapper.readValue(JSONResponse, typeFactory.constructCollectionType(List.class, Unit.class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

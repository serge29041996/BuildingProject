import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.dao.BuildingRepository;
import com.buildingproject.dao.DaoSpringClass;
import com.buildingproject.dao.UnitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {DaoSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class UnitTest {
  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private BuildingRepository buildingRepository;

  @Test
  public void testFindByNumberAndIdBuilding() {
    Building building = new Building("building_name", "building_address", 10, 10);
    building = buildingRepository.save(building);
    Unit unit = new Unit(1,2,40, true,true, building.getId());
    unitRepository.save(unit);

    Unit needUnit = unitRepository.findByNumberAndIdBuilding(1, building.getId());

    assertThat(needUnit).isEqualTo(unit);
  }

  @Test
  public void testFindByIdBuilding() {

  }
}

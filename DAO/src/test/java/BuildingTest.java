import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;
import com.buildingproject.dao.DaoSpringClass;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {DaoSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class BuildingTest {

  @Autowired
  private BuildingRepository buildingRepository;

  @Test
  public void testFindByName() {
    Building building = new Building("first", "last");
    buildingRepository.save(building);

    Building findByName = buildingRepository.findByName(building.getName());

    assertThat(findByName).isEqualTo(building);
  }

  @Test
  public void testFindByID() {
    Building building = new Building("second", "second");
    buildingRepository.save(building);

    Building findByID = buildingRepository.findOne(101L);

    assertThat(findByID).isEqualTo(building);
  }

  @Test
  public void testDeleteByID() {
    Building building = new Building("third", "third");
    building = buildingRepository.save(building);
    long idBuilding = building.getId();
    buildingRepository.delete(idBuilding);

    Building findByID = buildingRepository.findOne(idBuilding);
    assertThat(findByID).isNull();
  }

  @Test
  public void testCountEntity() {

    Building building = new Building("fourth", "fourth");
    building = buildingRepository.save(building);
    long countEntity = buildingRepository.count();

    assertThat(countEntity).isEqualTo(1);
  }

  @After
  public void deleteAllBuilding() {
    buildingRepository.deleteAll();
  }
}

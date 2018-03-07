import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;
import com.buildingproject.dao.BuildingTest1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {BuildingTest1.class})
/*
@DataJpaTest
*/
public class BuildingTest {

  @Autowired
  private BuildingRepository buildingRepository;

  @Test
  public void testFindByLastName() {
    Building building = new Building("first", "last");
    buildingRepository.save(building);

    List<Building> findByLastName = buildingRepository.findByName(building.getName());

    assertThat(findByLastName).extracting(Building::getName).containsOnly(building.getName());
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

    long countEntity = buildingRepository.count();

    assertThat(countEntity).isEqualTo(5);
  }

  //добавить AfterTest (AfterAll) для очистки БД
}

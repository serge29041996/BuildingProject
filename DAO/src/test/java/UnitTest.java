import com.buildingproject.commons.Building;
import com.buildingproject.commons.Unit;
import com.buildingproject.dao.BuildingRepository;
import com.buildingproject.dao.DaoSpringClass;
import com.buildingproject.dao.UnitRepository;
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
public class UnitTest {
  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private BuildingRepository buildingRepository;

  @Test
  public void testFindByNumberAndBuilding() {
    Building building = new Building("building_name", "building_address", 10);
    building = buildingRepository.save(building);
    Unit unit = new Unit(1,2,40, true,true,
                          building);
    unit = unitRepository.save(unit);

    Unit needUnit = unitRepository.findByNumberAndBuilding(1, building);

    assertThat(needUnit).isEqualTo(unit);
  }

  @Test
  public void testFindUnitsByBuilding() {
    Building building = new Building("1", "1",  10);
    building = buildingRepository.save(building);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);
    Unit unit2 = new Unit(2,1,30,false,false,
        building);
    Unit unit3 = new Unit(3,3,90,true,false,
        building);
    unitRepository.save(unit1);
    unitRepository.save(unit2);
    unitRepository.save(unit3);

    List<Unit> unitsOfBuilding = unitRepository.findByBuilding(building);

    assertThat(unitsOfBuilding.size()).isEqualTo(3);
  }

  @Test
  public void testUpdateUnit() {
    Building building = new Building("3", "3",  20);
    building = buildingRepository.save(building);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);

    unit1 = unitRepository.save(unit1);
    unit1.setNumber(10);
    unitRepository.save(unit1);

    Unit needUnit = unitRepository.findById(unit1.getId());

    assertThat(needUnit.getNumber()).isEqualTo(unit1.getNumber());

    List<Unit> units = unitRepository.findByBuilding(building);

    assertThat(units.size()).isEqualTo(1);
  }

  @Test
  public void testCascadeDelete() {
    Building building = new Building("3", "3",  3);
    building = buildingRepository.save(building);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);
    Unit unit2 = new Unit(2,1,30,false,false,
        building);
    Unit unit3 = new Unit(3,3,90,true,false,
        building);

    unitRepository.save(unit1);
    unitRepository.save(unit2);
    unitRepository.save(unit3);

    buildingRepository.delete(building.getId());

    List<Unit> unitsOfBuilding = unitRepository.findByBuilding(building);

    assertThat(unitsOfBuilding.size()).isEqualTo(0);
  }

  @Test
  public void testUnitDelete() {
    Building building = new Building("4", "4",  4);
    building = buildingRepository.save(building);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);
    Unit unit2 = new Unit(2,1,30,false,false,
        building);
    Unit unit3 = new Unit(3,3,90,true,false,
        building);

    unitRepository.save(unit1);
    unitRepository.save(unit2);
    unitRepository.save(unit3);

    Unit needUnit = unitRepository.findByNumberAndBuilding(2, building);

    unitRepository.delete(needUnit);

    needUnit = unitRepository.findById(needUnit.getId());

    assertThat(needUnit).isNull();

    List<Unit> unitsOfBuilding = unitRepository.findByBuilding(building);

    assertThat(unitsOfBuilding.size()).isEqualTo(2);
  }

  @After
  public void clearAllTestData() {
    unitRepository.deleteAll();
    buildingRepository.deleteAll();
  }
}

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

import java.util.ArrayList;
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
    buildingRepository.save(building);
    Unit unit = new Unit(1,2,40, true,true,
                          building);
    unitRepository.save(unit);
    // After saving unit this unit will be add to the building list, but we cannot compare units

    Unit needUnit = unitRepository.findByNumberAndBuilding(1, building);

    assertThat(needUnit).isEqualTo(unit);
  }

  @Test
  public void testFindByBuilding() {
    Building building = new Building("1", "1",  10);
    List<Unit> units = new ArrayList<>();
    units.add(new Unit(1,2,60,true,true,
                        building));
    units.add(new Unit(2,1,30,false,false,
                        building));
    units.add(new Unit(3,3,90,true,false,
                        building));

    building.setUnits(units);
    buildingRepository.save(building);
    // After saving all units also saved to the database

    List<Unit> unitsOfBuilding = unitRepository.findByBuilding(building);

    assertThat(unitsOfBuilding.size()).isEqualTo(3);
  }

  @Test
  public void testUnitsInBuilding() {
    Building building = new Building("2", "2",  2);
    Unit unit1 = new Unit(1,2,60,true,true,
                           building);
    Unit unit2 = new Unit(2,1,30,false,false,
                           building);
    Unit unit3 = new Unit(3,3,90,true,false,
                          building);

    buildingRepository.save(building);
    unitRepository.save(unit1);
    unitRepository.save(unit2);
    unitRepository.save(unit3);

    Building needBuilding = buildingRepository.findByName("2");

    assertThat(needBuilding.getUnits().size()).isEqualTo(3);
  }

  @Test
  public void testCascadeDelete() {
    Building building = new Building("3", "3",  3);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);
    Unit unit2 = new Unit(2,1,30,false,false,
        building);
    Unit unit3 = new Unit(3,3,90,true,false,
        building);
    List<Unit> units = new ArrayList<>();
    units.add(unit1);
    units.add(unit2);
    units.add(unit3);
    building.setUnits(units);
    Building needBuilding = buildingRepository.save(building);

    buildingRepository.delete(needBuilding.getId());

    List<Unit> unitsOfBuilding = unitRepository.findByBuilding(needBuilding);

    assertThat(unitsOfBuilding.size()).isEqualTo(0);
  }

  @Test
  public void testUnitDelete() {
    Building building = new Building("4", "4",  4);
    Unit unit1 = new Unit(1,2,60,true,true,
        building);
    Unit unit2 = new Unit(2,1,30,false,false,
        building);
    Unit unit3 = new Unit(3,3,90,true,false,
        building);
    List<Unit> units = new ArrayList<>();
    units.add(unit1);
    units.add(unit2);
    units.add(unit3);
    building.setUnits(units);
    Building needBuilding = buildingRepository.save(building);

    Unit needUnit = unitRepository.findByNumberAndBuilding(2, needBuilding);

    Building compareBuilding = buildingRepository.findById(needBuilding.getId());

    // this will not delete Unit from database
    unitRepository.delete(needUnit);

    //buildingRepository.flush();

    assertThat(needBuilding).isEqualTo(compareBuilding);

    assertThat(needBuilding.getUnits().contains(needUnit)).isEqualTo(true);
  }

  @After
  public void clearAllTestData() {
    unitRepository.deleteAll();
    buildingRepository.deleteAll();
  }
}

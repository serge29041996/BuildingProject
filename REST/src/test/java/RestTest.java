import com.buildingproject.commons.Building;
import com.buildingproject.service.IBuildingService;
import com.buildingproject.rest.RestSpringClass;
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
@ContextConfiguration(classes = {RestSpringClass.class})
public class RestTest {

  @Autowired
  private IBuildingService buildingService;

  @Test
  public void testFindAllBuildings (){
    List<Building> buildingList = buildingService.findAll();
    assertThat(buildingList.size()).isEqualTo(4);
  }
}
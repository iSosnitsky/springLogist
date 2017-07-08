package sbat.logist.ru.parser.exchanger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.parser.json.JsonDirection;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class RouteUpdaterTest {

    @Autowired
    private RouteUpdater routeUpdater;

    @Test
    public void testExecute() throws Exception {
        List<JsonDirection> list = new ArrayList<JsonDirection>() {{
            add(createJsonDirection("dirIdExt1", "route112"));
        }};
        routeUpdater.execute(list, null);
    }

    private JsonDirection createJsonDirection(String extId, String name) {
        return JsonDirection.builder()
                .directId(extId)
                .directName(name)
                .build();
    }
}
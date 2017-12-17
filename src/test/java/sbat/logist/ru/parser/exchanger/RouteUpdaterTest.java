package sbat.logist.ru.parser.exchanger;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.parser.DatabaseUpdaterService;
import sbat.logist.ru.parser.WatcherService;
import sbat.logist.ru.parser.command.FileToStringCommand;
import sbat.logist.ru.parser.command.FixJsonStringCommand;
import sbat.logist.ru.parser.command.JsonStringToObjectCommand;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.JsonDirection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class RouteUpdaterTest {
    private JsonStringToObjectCommand cmd = new JsonStringToObjectCommand();
    private FileToStringCommand command = new FileToStringCommand();
    private FixJsonStringCommand fixJsonStringCommand = new FixJsonStringCommand();
    @Autowired
    private RouteUpdater routeUpdater;
    @MockBean
    private WatcherService watcherService;

    @Test
    public void update() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("NOV.pkg").toURI());

        final String execute = command.execute(path);
        final String fixedString = fixJsonStringCommand.execute(execute);
        final Optional<Data1c> execute1 = cmd.execute(fixedString);
        Assert.assertTrue(execute1.isPresent());
        routeUpdater.execute(execute1.get().getDataFrom1C().getPackageData().getUpdateDirections(),
                execute1.get().getDataFrom1C().getPackageData().getUpdateRouteLists());
    }

    @Test
    public void testExecute() throws Exception {
        List<JsonDirection> list = new ArrayList<JsonDirection>() {{
            add(createJsonDirection("dirIdExt1", "route112"));
        }};
        routeUpdater.execute(list, Collections.emptyList());
    }

    private JsonDirection createJsonDirection(String extId, String name) {
        return JsonDirection.builder()
                .directId(extId)
                .directName(name)
                .build();
    }
}
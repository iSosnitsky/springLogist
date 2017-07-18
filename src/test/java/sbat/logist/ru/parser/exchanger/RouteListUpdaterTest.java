package sbat.logist.ru.parser.exchanger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.parser.WatcherService;
import sbat.logist.ru.parser.command.FileToStringCommand;
import sbat.logist.ru.parser.command.FixJsonStringCommand;
import sbat.logist.ru.parser.command.JsonStringToObjectCommand;
import sbat.logist.ru.parser.json.Data1c;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@SpringBootTest
@Import(Application.class)
@RunWith(SpringRunner.class)
public class RouteListUpdaterTest {
    private JsonStringToObjectCommand cmd = new JsonStringToObjectCommand();
    private FileToStringCommand command = new FileToStringCommand();
    private FixJsonStringCommand fixJsonStringCommand = new FixJsonStringCommand();
    @Autowired
    private RouteListUpdater routeListUpdater;
    @MockBean
    private WatcherService watcherService;

    @Test
    public void update() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("NOV.pkg").toURI());

        final String execute = command.execute(path);
        final String fixedString = fixJsonStringCommand.execute(execute);
        final Optional<Data1c> execute1 = cmd.execute(fixedString);
        Assert.assertTrue(execute1.isPresent());
        routeListUpdater.execute(execute1.get().getDataFrom1C().getPackageData().getUpdateRouteLists());
    }
}
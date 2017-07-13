package sbat.logist.ru.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.parser.command.FileToStringCommand;
import sbat.logist.ru.parser.command.FixJsonStringCommand;
import sbat.logist.ru.parser.command.JsonStringToObjectCommand;
import sbat.logist.ru.parser.json.Data1c;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Import(Application.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseUpdaterServiceTest {
    private JsonStringToObjectCommand cmd = new JsonStringToObjectCommand();
    private FileToStringCommand command = new FileToStringCommand();
    private FixJsonStringCommand fixJsonStringCommand = new FixJsonStringCommand();


    @Autowired
    private DatabaseUpdaterService databaseUpdaterService;

    @Test
    public void update() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("package.pkg").toURI());

        final String execute = command.execute(path);
        final String fixedString = fixJsonStringCommand.execute(execute);
        final Optional<Data1c> execute1 = cmd.execute(fixedString);
        Assert.assertTrue(execute1.isPresent());
        databaseUpdaterService.updateBackup(execute1.get());
    }

}
package sbat.logist.ru.parser.command;

import org.junit.Assert;
import org.junit.Test;
import sbat.logist.ru.parser.json.Data1c;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class JsonStringToObjectCommandTest {
    private JsonStringToObjectCommand cmd = new JsonStringToObjectCommand();
    private FileToStringCommand command = new FileToStringCommand();
    private FixJsonStringCommand fixJsonStringCommand = new FixJsonStringCommand();

    @Test
    public void testExecute() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("package.pkg").toURI());

        final String execute = command.execute(path);
        final String fixedString = fixJsonStringCommand.execute(execute);
        final Optional<Data1c> execute1 = cmd.execute(fixedString);
        Assert.assertTrue(execute1.isPresent());
    }

    @Test
    public void testMos() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("MOS_OLD.pkg").toURI());

        final String execute = command.execute(path);
        final String fixedString = fixJsonStringCommand.execute(execute);
        final Optional<Data1c> execute1 = cmd.execute(fixedString);
        Assert.assertTrue(execute1.isPresent());
    }
}
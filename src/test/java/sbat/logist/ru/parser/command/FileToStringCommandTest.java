package sbat.logist.ru.parser.command;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToStringCommandTest {
    private FileToStringCommand command;

    @BeforeMethod
    public void setUp() throws Exception {
        command = new FileToStringCommand();
    }

    @Test
    public void testExecute() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("package.pkg").toURI());

        Assert.assertTrue(command.execute(path).contains("dataFrom1C"));
    }

}
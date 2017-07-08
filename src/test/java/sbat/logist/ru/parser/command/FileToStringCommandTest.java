package sbat.logist.ru.parser.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToStringCommandTest {
    private FileToStringCommand command;

    @Before
    public void setUp() throws Exception {
        command = new FileToStringCommand();
    }

    @Test
    public void testExecute() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("package.pkg").toURI());

        Assert.assertTrue(command.execute(path).contains("dataFrom1C"));
    }

}
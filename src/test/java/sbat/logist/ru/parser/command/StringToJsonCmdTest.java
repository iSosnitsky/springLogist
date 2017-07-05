package sbat.logist.ru.parser.command;

import java.nio.file.Files;
import java.nio.file.Paths;

public class StringToJsonCmdTest {

    FixJsonStringCommand fixJsonStringCommand = new FixJsonStringCommand();

    public void test() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource("package.pkg").toURI())));
        final String execute = fixJsonStringCommand.execute(json);
    }
}
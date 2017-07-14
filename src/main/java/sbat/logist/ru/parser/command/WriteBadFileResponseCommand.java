package sbat.logist.ru.parser.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sbat.logist.ru.parser.command.Command;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Component("badResponse")
public class WriteBadFileResponseCommand implements Command<Path, Boolean> {
    private static final String RESPONSE_FILE_EXTENSION = ".ans";
    private final Path responseDir;

    @Autowired
    public WriteBadFileResponseCommand(@Qualifier("responseDirPath") Path responseDir) {
        this.responseDir = responseDir;
    }

    public Boolean execute(Path filePath) {
        try {
            writeBadFileResponse(filePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void writeBadFileResponse(Path filePath) throws IOException {
        final Path responsePath = responseDir.resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);

        try (FileWriter writer = new FileWriter(responsePath.toFile())) {
            writer.write(("Data format error . The details in the log file."));
        }
    }
}

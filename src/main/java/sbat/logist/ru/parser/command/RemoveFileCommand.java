package sbat.logist.ru.parser.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Objects;

@Component("remove")
public class RemoveFileCommand implements Command<Path, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger("main");

    public Boolean execute(Path filePath) {
        Objects.requireNonNull(filePath);
        return removeIncomingFile(filePath);
    }

    private boolean removeIncomingFile(Path filePath) {
        logger.info("start delete file: [{}]", filePath);
        if (filePath.toFile().delete()) {
            logger.info("file was deleted: [{}]", filePath);
            return true;
        } else {
            logger.error("file wasn't deleted: [{}]", filePath);
            return false;
        }
    }
}

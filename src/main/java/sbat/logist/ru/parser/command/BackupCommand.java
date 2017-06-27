package sbat.logist.ru.parser.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component("backup")
public class BackupCommand implements Command<Path,Boolean> {
    private static final Logger logger = LoggerFactory.getLogger("name");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

    private final Path backupDir;

    @Autowired
    public BackupCommand(@Qualifier("backupDirPath") Path backupDir) {
        this.backupDir = backupDir;
    }

    public Boolean execute(Path filePath) {
        Objects.requireNonNull(filePath);
        try {
            return copyToBackup(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean copyToBackup(Path filePath) throws IOException {
        String currentDateString = "_" + dateFormat.format(new Date()) + "_";
        String backupFileName = currentDateString + filePath.getFileName();
        Path backupFilePath = backupDir.resolve(backupFileName);

        logger.info("start backup file: [{}]", filePath);
        Files.copy(filePath, backupFilePath);
        logger.info("file was backuped: [{}]", backupFilePath);
        return true;
    }
}

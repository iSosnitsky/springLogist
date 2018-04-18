package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
@Profile({"restore"})
public class BackupRestoringService implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final Path watchPath;
    private final CommandExecutorService commandExecutorService;
    private final Comparator<Path> comparator;

    @Autowired
    public BackupRestoringService(
            @Qualifier("jsonDataDirPath") Path path,
            CommandExecutorService commandExecutorService
    ) {
        this.watchPath = path;
        this.commandExecutorService = commandExecutorService;
        comparator = (o1, o2) -> {
            String firstName = o1.getFileName().toString();
            String secondName = o2.getFileName().toString();
            return firstName.compareToIgnoreCase(secondName);
        };
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("start restoring from backup service: [{}]", watchPath.toString());
        Files.walk(watchPath)
                .filter(Files::isRegularFile)
                .sorted(comparator)
                .forEach(f -> {
                    try {
                        commandExecutorService.executeAll(f);
                    } catch (Exception e) {
                        logger.error("CommandExecutor error", e);
                    }
                });
    }
}

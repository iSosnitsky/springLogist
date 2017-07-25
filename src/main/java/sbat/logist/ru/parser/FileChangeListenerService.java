package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileChangeListenerService implements Watchable {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final String TEMP_FILE_EXTENSION = ".tmp";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final CommandExecutorService commandExecutorService;

    @Autowired
    public FileChangeListenerService(CommandExecutorService commandExecutorService) {
        this.commandExecutorService = commandExecutorService;
    }

    @Override
    public void onFileCreate(Path filePath) {

        // start executor when downloading finishes
        if (filePath.toString().endsWith(TEMP_FILE_EXTENSION))
            return;

        executorService.submit(() -> {
            waitForFileReleaseLock(filePath);
            try {
                commandExecutorService.executeAll(filePath);
            } catch (Exception e) {
                logger.error("CommandExecutor error", e);
            }
        });
    }


    @Override
    public void onFileModify(Path filePath) {

        if(!filePath.toString().endsWith(TEMP_FILE_EXTENSION)){
            logger.info("file modified: {}", filePath);
        }

    }

    @Override
    public void onFileDelete(Path filePath) {
        logger.info("file deleted: {}", filePath);
    }

    public void close() {
        executorService.shutdown();
    }

    private void waitForFileReleaseLock(Path filePath) {
        for (int i = 0; i < 10; i++) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
                bufferedReader.close();
                break;
            } catch (FileSystemException e) {
                logger.warn("file [{}] locked, wait for release...", filePath);
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e1) {/*NOPE*/}

            } catch (IOException e) {
                    /*NOPE*/
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {/*NOPE*/}
            }
        }
    }


}

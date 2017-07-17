package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Service
public class WatcherService implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final Path watchPath;
    private final FileChangeListenerService fileChangeListener;

    @Autowired
    public WatcherService(
            @Qualifier("jsonDataDirPath") Path path,
            FileChangeListenerService fileChangeListenerService
    ) throws IOException {
        this.watchPath = path;
        this.fileChangeListener = fileChangeListenerService;
    }

    @Override
    public void run(String... strings) throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        logger.info("start watch service for [{}]", watchPath.toString());
        WatchKey watchKey = watchPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        while (!Thread.currentThread().isInterrupted()){

            WatchKey key;

            try {
                key = watchService.take();
            }
            catch (InterruptedException ex) {
                logger.error("watch thread interrupted");
                return;
            }

            final List<WatchEvent<?>> eventList = key.pollEvents();

            for (WatchEvent<?> genericEvent: eventList) {

                final WatchEvent.Kind<?> eventKind = genericEvent.kind();
                if (eventKind == OVERFLOW) {
                    continue; // pending events for loop
                }

                final WatchEvent pathEvent = (WatchEvent) genericEvent;

                if (eventKind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    final Path createdFileName = watchPath.resolve((Path)pathEvent.context());
                    if (fileChangeListener != null) {
                        fileChangeListener.onFileCreate(createdFileName);
                    }
                }
                if (eventKind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                    final Path deletedFileName = watchPath.resolve((Path)pathEvent.context());
                    if (fileChangeListener != null) {
                        fileChangeListener.onFileDelete(deletedFileName);
                    }
                }
                if (eventKind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    final Path modifiedFileName = watchPath.resolve((Path)pathEvent.context());
                    if (fileChangeListener != null) {
                        fileChangeListener.onFileModify(modifiedFileName);
                    }
                }

            }

            boolean validKey = key.reset();

            if (!validKey) {
                logger.error("Invalid key");
                break; // infinite for loop
            }

        }
    }
}

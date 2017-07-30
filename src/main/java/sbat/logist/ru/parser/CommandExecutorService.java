package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sbat.logist.ru.parser.command.Command;
import sbat.logist.ru.parser.command.JsonStringToObjectCommand;
import sbat.logist.ru.parser.command.TransactionResult;
import sbat.logist.ru.parser.json.Data1c;

import java.nio.file.Path;

@Service
public class CommandExecutorService {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final Command<Path, Boolean> writeBadFileResponseCommand;
    private final Command<Path, Boolean> removeFileCommand;
    private final Command<Path, Boolean> backupCommand;
    private final Command<Path, String> toMessageCommand;
    private final Command<String, String> fixJsonCommand;
    private final Command<TransactionResult, Boolean> writeDbResponseCommand;
    private final DatabaseUpdaterService databaseUpdaterService;
    private final Path responseDir;
    private final Path backupDir;

    @Autowired
    public CommandExecutorService(
            @Qualifier("responseDirPath") Path responseDirPath,
            @Qualifier("backupDirPath") Path backupDir,
            @Qualifier("badResponse") Command<Path, Boolean> writeBadFileResponseCommand,
            @Qualifier("remove") Command<Path, Boolean> removeFileCommand,
            @Qualifier("backup") Command<Path, Boolean> backupCommand,
            @Qualifier("toMessage") Command<Path, String> toMessageCommand,
            @Qualifier("fixJson") Command<String, String> fixJsonCommand,
            @Qualifier("writeDb") Command<TransactionResult, Boolean> writeDbWorkResponseCommand,
            DatabaseUpdaterService databaseUpdaterService
    ) {
        this.responseDir = responseDirPath;
        this.backupDir = backupDir;
        this.removeFileCommand = removeFileCommand;
        this.writeBadFileResponseCommand = writeBadFileResponseCommand;
        this.backupCommand = backupCommand;
        this.toMessageCommand = toMessageCommand;
        this.fixJsonCommand = fixJsonCommand;
        this.writeDbResponseCommand = writeDbWorkResponseCommand;
        this.databaseUpdaterService = databaseUpdaterService;
    }

    public void executeAll(Path filePath) throws Exception {
        if (!backupCommand.execute(filePath)) {
            throw new IllegalStateException("can't copy into backup directory");
        }

        // read incoming file as string
        final String fileAsString;
        try {
            fileAsString = toMessageCommand.execute(filePath);
        } catch (IllegalStateException e) {
            logger.error("error while reading file: {}. Error: {}", filePath, e);
            deleteIncomingFile(filePath);
            writeBadFileResponse(filePath);
            return;
        }

        String fixedJsonString;
        try {
            fixedJsonString = fixJsonCommand.execute(fileAsString);
        } catch (IllegalArgumentException e) {
            logger.error("cant parse file{}, Error: {}", filePath, e);
            deleteIncomingFile(filePath);
            writeBadFileResponse(filePath);
            return;
        }
        // read JsonObject as bean object

        JsonStringToObjectCommand jsonToBeanCmd = new JsonStringToObjectCommand();
        Data1c data1c;
        try {
            data1c = jsonToBeanCmd.execute(fixedJsonString)
                    .orElseThrow(Exception::new);
        } catch (Exception e) {
            logger.error("Can't map to object.", e);
            deleteIncomingFile(filePath);
            writeBadFileResponse(filePath);
            return;
        }

        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setServer(data1c.getDataFrom1C().getServer());
        transactionResult.setPackageNumber(data1c.getDataFrom1C().getPackageNumber());

        try {
            databaseUpdaterService.updateBackup(data1c);
            transactionResult.setStatus(TransactionResult.OK_STATUS);
        } catch (Exception e) {
            logger.warn("Error ocurred while updating database: ", e);
            deleteIncomingFile(filePath);
            transactionResult.setStatus(TransactionResult.ERROR_STATUS);
            writeDbFileResponse(transactionResult);
            return;
        }

        // write response into response directory
        writeDbFileResponse(transactionResult);
        // remove incoming file
        deleteIncomingFile(filePath);
    }



    private void deleteIncomingFile(Path filePath) {
        removeFileCommand.execute(filePath);
    }

    private void writeBadFileResponse(Path filePath) {
        writeBadFileResponseCommand.execute(filePath);
    }

    private void writeDbFileResponse(TransactionResult transactionResult) {
        if (!writeDbResponseCommand.execute(transactionResult)) {
            throw new RuntimeException("Can't write db file response");
        }

    }

}


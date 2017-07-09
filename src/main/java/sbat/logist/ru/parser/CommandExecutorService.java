package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sbat.logist.ru.parser.command.Command;
import sbat.logist.ru.parser.command.JsonStringToObjectCommand;
import sbat.logist.ru.parser.command.WriteBadFileResponseCommand;
import sbat.logist.ru.parser.json.Data1c;

import java.nio.file.Path;

@Service
public class CommandExecutorService {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final WriteBadFileResponseCommand writeBadFileResponseCommand;
    private final Command<Path, Boolean> removeFileCommand;
    private final Command<Path, Boolean> backupCommand;
    private final Command<Path, String> toMessageCommand;
    private final Command<String, String> fixJsonCommand;
    private final Path responseDir;
    private final Path backupDir;

    @Autowired
    public CommandExecutorService(
            @Qualifier("responseDirPath") Path responseDirPath,
            @Qualifier("backupDirPath") Path backupDir,
            WriteBadFileResponseCommand writeBadFileResponseCommand,
            @Qualifier("remove") Command<Path, Boolean> removeFileCommand,
            @Qualifier("backup") Command<Path, Boolean> backupCommand,
            @Qualifier("toMessage") Command<Path, String> toMessageCommand,
            @Qualifier("fixJson") Command<String, String> fixJsonCommand
    ) {
        this.responseDir = responseDirPath;
        this.backupDir = backupDir;
        this.removeFileCommand = removeFileCommand;
        this.writeBadFileResponseCommand = writeBadFileResponseCommand;
        this.backupCommand = backupCommand;
        this.toMessageCommand = toMessageCommand;
        this.fixJsonCommand = fixJsonCommand;
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

        // load data into database
        /*BeanIntoDataBaseCmd beanIntoDataBaseCmd = new BeanIntoDataBaseCmd(dataFrom1c, dbManager);
        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setServer(dataFrom1c.getServer());
        transactionResult.setPackageNumber(dataFrom1c.getPackageNumber().intValue());

        try {
            beanIntoDataBaseCmd.execute();
            transactionResult.setStatus(TransactionResult.OK_STATUS);
        } catch (DBCohesionException e) {
            logger.warn(e);
            rollback();
            deleteIncomingFile();
            transactionResult.setStatus(TransactionResult.ERROR_STATUS);
            writeDbFileResponse(transactionResult);
            return;
        } catch (SQLException | ResourceInitException e) {
            logger.error(e);
            rollback();
            deleteIncomingFile();
            transactionResult.setStatus(TransactionResult.ERROR_STATUS);
            writeDbFileResponse(transactionResult);
            return;
        }

        /*
        // write response into response directory
        writeDbFileResponse(transactionResult);
        // remove incoming file
        deleteIncomingFile();
  */
    }

//    private void rollback() throws FatalException {
//        try {
//            dbManager.getConnection().rollback();
//        } catch (SQLException ex) {
//            throw new FatalException(ex);
//        }
//    }



    private void deleteIncomingFile(Path filePath) {
        removeFileCommand.execute(filePath);
    }

    private void writeBadFileResponse(Path filePath) {
        writeBadFileResponseCommand.execute(filePath);
    }
/*
    private void writeDbFileResponse(TransactionResult transactionResult) throws FatalException {
        WriteDbWorkResponseCmd writeDbWorkResponseCmd = new WriteDbWorkResponseCmd(filePath, responseDir, transactionResult);
        try {
            writeDbWorkResponseCmd.execute();
        } catch (IOException e) {
            throw new FatalException(e);
        }

    }*/

}


package sbat.logist.ru.parser.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

@Component("writeDb")
public class WriteDbWorkResponseCommand implements Command<TransactionResult, Boolean> {
    private static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final Path responseDir;

    @Autowired
    public WriteDbWorkResponseCommand(@Qualifier("responseDirPath") Path responseDir) {
        this.responseDir = responseDir;
    }

    @Override
    public Boolean execute(TransactionResult transactionResult) {
        logger.info("Start write result data into response directory");
        Path responsePath = responseDir.resolve(transactionResult.getServer() + RESPONSE_FILE_EXTENSION);
        String resultString = transactionResult.toString();
        try (PrintWriter out = new PrintWriter(responsePath.toFile())) {
            out.println(resultString);
            logger.info("Response data were successfully written");
        } catch (FileNotFoundException e) {
            logger.error("Can't write result to file", e);
            return false;
        }
        return true;
    }
}

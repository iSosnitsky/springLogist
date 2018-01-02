package sbat.logist.ru.parser.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.PacketStatus;
import sbat.logist.ru.transport.domain.ExchangeLog;
import sbat.logist.ru.transport.repository.ExchangeLogRepository;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Date;

@Component("writeDb")
public class WriteDbWorkResponseCommand implements Command<TransactionResult, Boolean> {
    private static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final ExchangeLogRepository exchangeLogRepository;
    private final Path responseDir;

    @Autowired
    public WriteDbWorkResponseCommand(@Qualifier("responseDirPath") Path responseDir, ExchangeLogRepository exchangeLogRepository) {
        this.responseDir = responseDir;
        this.exchangeLogRepository = exchangeLogRepository;
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
        logger.info("Start write response into log table");
        final ExchangeLog response = ExchangeLog.builder().packetId(transactionResult.getPackageNumber()).server(transactionResult.getServer()).packetStatus(PacketStatus.valueOf(transactionResult.getStatus())).date(new Date()).build();
        try {
            exchangeLogRepository.save(response);
            logger.info("Response successfully written into ExchangeLog repository");
        } catch (Exception e){
            logger.error("Couldn't write response into log table " + e);
        }
        return true;
    }
}

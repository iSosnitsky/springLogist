package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.transport.repository.MatViewBigSelectRepository;

@Component
public class MatViewUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final MatViewBigSelectRepository matViewBigSelectRepository;

    @Autowired
    public MatViewUpdater(MatViewBigSelectRepository matViewBigSelectRepository) {
        this.matViewBigSelectRepository = matViewBigSelectRepository;
    }

    public void execute() {
        logger.info("START refresh materialized view");
        matViewBigSelectRepository.refresh();
        logger.info("END refresh materialized view");
    }
}

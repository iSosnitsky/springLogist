package sbat.logist.ru.parser.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbat.logist.ru.parser.json.Data1c;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class JsonStringToObjectCommand implements Command<String, Optional<Data1c>> {
    private static final Logger logger = LoggerFactory.getLogger("main");

    public Optional<Data1c> execute(String json) {
        Objects.requireNonNull(json);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.of(mapper.readValue(json, Data1c.class));
        } catch (IOException e) {
            logger.error("Can't parse string.", e);
            return Optional.empty();
        }
    }

}

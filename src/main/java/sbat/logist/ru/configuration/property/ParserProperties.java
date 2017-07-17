package sbat.logist.ru.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "parser")
public class ParserProperties {
    private String jsonDataDir;
    private String backupDir;
    private String responseDir;
}

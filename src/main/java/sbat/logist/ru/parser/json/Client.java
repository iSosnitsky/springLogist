package sbat.logist.ru.parser.json;

import lombok.Data;

@Data
public class Client {
    private String clientId;
    private String clientName;
    private String clientINN;
    private String clientPassword;
}

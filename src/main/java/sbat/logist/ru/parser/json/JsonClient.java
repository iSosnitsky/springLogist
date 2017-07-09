package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonClient {
    private String clientId;
    private String clientName;
    @JsonProperty("clientINN")
    private String clientInn;
    private String clientPassword;

    public boolean hasValidPassword() {
        return (clientPassword!= null && !clientPassword.isEmpty());
    }
}

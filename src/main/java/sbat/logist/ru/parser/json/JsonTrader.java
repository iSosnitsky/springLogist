package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonTrader {
    private String traderId;
    private String traderName;
    @JsonProperty("traderEMail")
    private String traderEmail;
    private String traderPhone;
    private String traderOffice;
    private String traderLogin;
    private String traderPassword;

    public boolean hasValidLoginAndPassword() {
        return traderLogin != null && !traderLogin.isEmpty() && traderPassword != null && !traderPassword.isEmpty();
    }
}

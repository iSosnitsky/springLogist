package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAddress {
    private String addressId;
    private String addressShot;
    private String addressFull;
    private String deliveryAreaId;
}

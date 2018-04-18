package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonPoint {
    private String pointId;
    private String pointName;
    private String pointAddress;
    private String pointType;
    private String pointEmail;
    private String responsiblePersonId;
}

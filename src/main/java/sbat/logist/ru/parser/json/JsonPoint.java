package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class JsonPoint {
    private String pointId;
    private String pointName;
    private String pointAdress;
    private String pointType;
    private String pointEmail;
    private String responsiblePersonId;
}

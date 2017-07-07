package sbat.logist.ru.parser.json;

import lombok.Data;

@Data
public class JsonPoint {
    private String pointId;
    private String pointName;
    private String pointAddress;
    private String pointType;
    private String pointEmails;
    private String responsiblePersonId;
}

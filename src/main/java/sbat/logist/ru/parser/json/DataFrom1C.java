package sbat.logist.ru.parser.json;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DataFrom1C {
    private String server;
    private Integer packageNumber;
    private Date created;
    private PackageData packageData;
}

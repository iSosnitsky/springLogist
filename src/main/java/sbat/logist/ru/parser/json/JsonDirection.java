package sbat.logist.ru.parser.json;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonDirection {
    private String directId;
    private String directName;
}

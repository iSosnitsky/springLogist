package sbat.logist.ru.parser.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Data1c {
    private String json;
    private DataFrom1C dataFrom1C;
}

package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Status {
    private String requestId;
    @JsonProperty("num_boxes")
    private Integer numBoxes;
    private String status;
    private String timeOutStatus;

    @JsonProperty("Comment")
    private String comment;
}

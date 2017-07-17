package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class JsonStatus {
    private String requestId;
    @JsonProperty("num_boxes")
    private Integer numBoxes;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yy,HH:mm:ss")
    private Date timeOutStatus;
    @JsonProperty("Comment")
    private String comment;
}

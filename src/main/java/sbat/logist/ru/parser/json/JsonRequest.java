package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class JsonRequest {
    private String requestId;
    private String requestNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date requestDate;
    private String invoiceNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date invoiceDate;
    private String documentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date documentDate;
    private String firma;
    private String storage;
    private String clientId;
    private String addressId;
    private String contactName;
    private String contactPhone;
    private String deliveryOption;
    private String traderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date deliveryDate;
}

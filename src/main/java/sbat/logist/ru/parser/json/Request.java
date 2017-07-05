package sbat.logist.ru.parser.json;

import lombok.Data;

import java.util.Date;

@Data
public class Request {
    private String requestId;
    private String requestNumber;
    private Date requestDate;
    private String invoiceNumber;
    private Date invoiceDate;
    private String documentNumber;
    private Date documentDate;
    private String firma;
    private String storage;
    private String clientId;
    private String addressId;
    private String contactName;
    private String contactPhone;
    private String deliveryOption;
    private String traderId;
    private Date deliveryDate;
}

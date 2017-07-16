package sbat.logist.ru.backup;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "exchange")
@Data
public class Exchange {
    @EmbeddedId
    private ExchangeId exchangeId;

    @Column(name = "PACKAGEADDED")
    private Timestamp packageAdded;

    @Column(name = "PACKAGECREATED")
    private Date packageCreated;

    @Column(name = "PACKAGEDATA", columnDefinition="LONGTEXT")
    private String packageData;
}
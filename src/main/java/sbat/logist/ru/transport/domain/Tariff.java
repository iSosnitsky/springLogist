package sbat.logist.ru.transport.domain;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.tools.javac.code.Attribute;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.jdo.annotations.*;
import javax.persistence.*;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@Table(name="TARRIFS")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TARRIFID")
    private int tariffID;

    @Column(name="COST")
    private BigDecimal cost;

    @Column(name="COST_PER_POINT")
    private BigDecimal costPerPoint;

    @Column(name="COST_PER_HOUR")
    private BigDecimal costPerHour;

    @Column(name="CAPACITY")
    private BigDecimal capacity;

    @Column(name="CARRIER")
    private String carrier;

    @Column(name="COST_PER_VOX")
    private BigDecimal costPerBox;
}

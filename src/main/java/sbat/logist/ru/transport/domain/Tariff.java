package sbat.logist.ru.transport.domain;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="TARIFFS")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TARIFFID")
    private Long id;

    @Column(name="COST")
    private Double cost;

    @Column(name="COST_PER_POINT")
    private Double costPerPoint;

    @Column(name="tariffName")
    private String tariffName;

    @Column(name="COST_PER_HOUR")
    private Double costPerHour;

    @Column(name="CAPACITY")
    private Double capacity;

    @Column(name="CARRIER")
    private String carrier;

    @Column(name="COST_PER_BOX")
    private Double costPerBox;
}

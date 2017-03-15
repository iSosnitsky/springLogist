package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "data_sources")
public class DataSource {
    @Id
    @GeneratedValue
    @Column(name = "DATASOURCEID")
    private String dataSourceId;

}

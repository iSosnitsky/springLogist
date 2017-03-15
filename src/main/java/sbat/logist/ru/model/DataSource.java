package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "distances_between_points")
public class DistanceBetweenPoints {
    @EmbeddedId
    private DistanceBetweenPointsId distanceBetweenPointsId;
    private Integer distance;
}

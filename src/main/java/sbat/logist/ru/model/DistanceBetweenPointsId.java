package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class DistanceBetweenPointsId implements Serializable {
    @ManyToOne
    @NotNull
    @JoinColumn(name = "POINTIDFIRST")
    private Point pointFirst;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "POINTIDSECOND")
    private Point pointSecond;
}

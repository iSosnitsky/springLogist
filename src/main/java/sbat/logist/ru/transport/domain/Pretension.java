package sbat.logist.ru.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pretensions")
public class Pretension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRETENSIONID")
    private Integer pretensionId;

    @Column(name="PRETENSIONCOMMENT")
    private String pretensionComment;

    @Column(name="REQUESTIDEXTERNAL", nullable = false)
    private String requestIdExternal;

    @Column(name="PRETENSIONSTATUS", nullable = false)
    private String pretensionStatus = "OPENED";

    @Column(name="PRETENSIONCATHEGORY", nullable = false)
    private String pretensionCategory;

    @Column(name="SUM")
    private Double sum;

    @Column(name="POSITIONNUMBER")
    private String positionNumber;

    @Column(name="DATEADDED")
    private Date dateAdded = new Date();
}

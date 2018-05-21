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

//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name="REQUESTIDEXTERNAL", referencedColumnName = "REQUESTIDEXTERNAL")
//        private Request request;
    @Column(name="REQUESTIDEXTERNAL")
    private String requestIdExternal;

    @Column(name="PRETENSIONSTATUS")
    private String pretensionStatus;

    @Column(name="PRETENSIONCATHEGORY")
    private String pretensionCathegory;

    @Column(name="SUM")
    private Double sum;

    @Column(name="POSITIONNUMBER")
    private String positionNumber;

    @Column(name="DATEADDED")
    private Date dateAdded;

}

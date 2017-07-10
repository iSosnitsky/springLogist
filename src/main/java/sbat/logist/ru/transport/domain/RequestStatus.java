package sbat.logist.ru.transport.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="REQUEST_STATUSES")
public class RequestStatus {
    @Id
    @Column(name="REQUESTSTATUSID")
    private String requestStatusId;

    @Column(name="REQUESTSTATUSRUSNAME")
    private String requestStatusRusName;

    @Column(name="SEQUENCE")
    private Integer sequence;
}

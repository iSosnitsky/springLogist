package sbat.logist.ru.transport;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clients", indexes = {
        @Index(name = "external_id_datasource_index", columnList = "CLIENTIDEXTERNAL,DATASOURCEID", unique = true),
        @Index(name = "datasource_index", columnList = "DATASOURCEID")
})
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "CLIENTID")
    private Long clientID;

    @NotNull
    @Column(name = "CLIENTIDEXTERNAL")
    private String clientIDExternal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DATASOURCEID")
    private DataSource dataSource;

    private String inn;

    @Column(name = "CLIENTNAME")
    private String clientName;

    private String kpp;

    @Column(name = "CORACCOUNT")
    private String corAccount;

    @Column(name = "CURACCOUNT")
    private String curAccount;

    private String bik;

    @Column(name = "BANKNAME")
    private String bankName;

    @Column(name = "CONTRACTNUMBER")
    private String contractNumber;

    @Column(name = "DATEOFSIGNING")
    private Date dateOfSigning;

    @Column(name = "STARTCONTRACTDATE")
    private Date startContractDate;

    @Column(name = "ENDCONTRACTDATE")
    private Date endContractDate;
}

package sbat.logist.ru.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.constant.DataSource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients", indexes = {
        @Index(name = "external_id_datasource_index", columnList = "CLIENTIDEXTERNAL,DATASOURCEID", unique = true),
        @Index(name = "datasource_index", columnList = "DATASOURCEID")
})
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENTID")
    private Long clientID;

    @NotNull
    @Column(name = "CLIENTIDEXTERNAL")
    private String clientIDExternal;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @Column(name = "INN", nullable = false)
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

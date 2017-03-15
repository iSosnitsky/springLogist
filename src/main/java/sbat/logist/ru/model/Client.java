package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "clients")
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "CLIENTID")
    private Long clientID;

    @Column(name = "CLIENTIDEXTERNAL")
    private String clientIDExternal;

    @Column(name = "DATASOURCEID")
    private String dataSourceID;

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

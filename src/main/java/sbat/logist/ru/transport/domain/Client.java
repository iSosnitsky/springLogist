package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.util.generator.RandomStringGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
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
    @JsonView(DataTablesOutput.View.class)
    private Long clientID;

    @NotNull
    @Column(name = "CLIENTIDEXTERNAL")
    @JsonView(DataTablesOutput.View.class)
    private String clientIDExternal;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private DataSource dataSource;

    @Column(name = "INN", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    private String inn;

    @Column(name = "CLIENTNAME")
    @JsonView(DataTablesOutput.View.class)
    private String clientName;

    @JsonView(DataTablesOutput.View.class)
    private String kpp;

    @Column(name = "CORACCOUNT")
    @JsonView(DataTablesOutput.View.class)
    private String corAccount;

    @Column(name = "CURACCOUNT")
    @JsonView(DataTablesOutput.View.class)
    private String curAccount;

    @JsonView(DataTablesOutput.View.class)
    private String bik;

    @Column(name = "BANKNAME")
    @JsonView(DataTablesOutput.View.class)
    private String bankName;

    @Column(name = "CONTRACTNUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String contractNumber;

    @Column(name = "DATEOFSIGNING")
    @JsonView(DataTablesOutput.View.class)
    private Date dateOfSigning;

    @Column(name = "STARTCONTRACTDATE")
    @JsonView(DataTablesOutput.View.class)
    private Date startContractDate;

    @Column(name = "ENDCONTRACTDATE")
    @JsonView(DataTablesOutput.View.class)
    private Date endContractDate;


    @PrePersist
    private void checkClient(){
        if(this.dataSource==null) this.dataSource = DataSource.ADMIN_PAGE;
        if(this.clientIDExternal==null) this.clientIDExternal="LSS-"+RandomStringGenerator.randomAlphaNumeric(10);
    }
}

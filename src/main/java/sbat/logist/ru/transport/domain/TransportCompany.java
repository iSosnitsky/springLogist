package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;

@Entity
@Data
@Table(name="TRANSPORT_COMPANIES")
public class TransportCompany {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @Column(name="NAME")
    @JsonView(DataTablesOutput.View.class)
    private String name;

    @Column(name="SHORT_NAME")
    @JsonView(DataTablesOutput.View.class)
    private String shortName;

    @Column(name="INN")
    @JsonView(DataTablesOutput.View.class)
    private String Inn;

    @Column(name="BIK")
    @JsonView(DataTablesOutput.View.class)
    private String Bik;

    @Column(name="COR_ACCOUNT")
    @JsonView(DataTablesOutput.View.class)
    private String corAccount;

    @Column(name="CUR_ACCOUNT")
    @JsonView(DataTablesOutput.View.class)
    private String curAccount;

    @Column(name="BANK_NAME")
    @JsonView(DataTablesOutput.View.class)
    private String bankName;

    @Column(name="LEGAL_ADDRESS")
    @JsonView(DataTablesOutput.View.class)
    private String legalAddress;

    @Column(name="POST_ADDRESS")
    @JsonView(DataTablesOutput.View.class)
    private String postAddress;

    @Column(name="KEYWORDS")
    @JsonView(DataTablesOutput.View.class)
    private String keywords;

    @Column(name="DIRECTOR_FULLNAME")
    @JsonView(DataTablesOutput.View.class)
    private String directorFullname;

    @Column(name="CHIEF_ACC_FULLNAME")
    @JsonView(DataTablesOutput.View.class)
    private String chiefAccFullname;
}

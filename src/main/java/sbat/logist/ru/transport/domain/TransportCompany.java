package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="TRANSPORT_COMPANIES")
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
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
    private String inn;

    @Column(name="BIK")
    @JsonView(DataTablesOutput.View.class)
    private String bik;

    @Column(name="KPP")
    @JsonView(DataTablesOutput.View.class)
    private String kpp;

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

    @OneToMany(mappedBy = "transportCompany")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "transportCompany")
    private List<Driver> drivers;

    @Column(name="DELETED")
    private short deleted;
}

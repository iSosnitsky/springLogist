package sbat.logist.ru.transport.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="TRANSPORT_COMPANIES")
public class TransportCompany {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="SHORT_NAME")
    private String shortName;

    @Column(name="INN")
    private String Inn;

    @Column(name="BIK")
    private String Bik;

    @Column(name="COR_ACCOUNT")
    private String corAccount;

    @Column(name="CUR_ACCOUNT")
    private String curAccount;

    @Column(name="BANK_NAME")
    private String bankName;

    @Column(name="LEGAL_ADDRESS")
    private String legalAddress;

    @Column(name="POST_ADDRESS")
    private String postAddress;

    @Column(name="KEYWORDS")
    private String keywords;

    @Column(name="DIRECTOR_FULLNAME")
    private String directorFullname;

    @Column(name="CHIEF_ACC_FULLNAME")
    private String chiefAccFullname;
}

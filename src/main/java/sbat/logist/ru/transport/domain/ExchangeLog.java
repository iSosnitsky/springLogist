package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.constant.PacketStatus;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "exchange_log")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExchangeLog {
    @Column(name="ENTRY_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @Column(name="PACKET_ID")
    private int packetId;

    @Column(name = "DATE")
    @JsonFormat(pattern = "d/M/yyyy HH:mm")
    private Date date;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private PacketStatus packetStatus;

    @Column(name= "SERVER")
    private String server;
}

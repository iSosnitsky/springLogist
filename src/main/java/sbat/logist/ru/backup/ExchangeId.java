package sbat.logist.ru.backup;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ExchangeId implements Serializable {
    @Column(name = "PACKAGENUMBER", nullable = false)
    private int packageNumber;

    @Column(name = "SERVERNAME", nullable = false)
    private String serverName;

    public ExchangeId(String serverName, int packageNumber) {
        this.packageNumber = packageNumber;
        this.serverName = serverName;
    }
}

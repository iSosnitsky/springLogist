package sbat.logist.ru.parser.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Data
public class TransactionResult {
    public static final String OK_STATUS = "OK";
    public static final String ERROR_STATUS = "ERROR";
    private Path filePath;
    private String status;
    private String server;
    private Integer packageNumber;

    @Override
    public String toString() {
        return server + ";" + packageNumber + ";" + status;
    }
}

package sbat.logist.ru.parser.response;

public class TransactionResult {
    public static final String OK_STATUS = "OK";
    public static final String ERROR_STATUS = "ERROR";

    private String server;
    private Integer packageNumber;
    private String status;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(Integer packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOk() {
        return getStatus().equals(OK_STATUS);
    }

    @Override
    public String toString() {
        return server + ";" + packageNumber + ";" + status;
    }
}
package sbat.logist.ru.constant;

import lombok.Getter;

public enum DataSource {
    ADMIN_PAGE("Создано из системы"), LOGIST_1C("Создано из 1С"), REQUESTS_ASSIGNER("Создано автоматически");

    @Getter
    private final String dataSourceName;

    DataSource(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

}

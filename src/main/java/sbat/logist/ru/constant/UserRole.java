package sbat.logist.ru.constant;

import lombok.Getter;

public enum UserRole {
    ADMIN("Админ"),
    CLIENT_MANAGER("Клиент"),
    DISPATCHER("Диспетчер"),
    MARKET_AGENT("Торговый представитель"),
    TEMP_REMOVED("Временно удален"),
    W_DISPATCHER("Диспетчер склада");

    @Getter
    private final String rusRoleName;

    UserRole(String rusRoleName) {
        this.rusRoleName = rusRoleName;
    }
}
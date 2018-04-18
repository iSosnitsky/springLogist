package sbat.logist.ru.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN("Админ", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    CLIENT_MANAGER("Клиент", Arrays.asList("sasat", "plus", "lejat")),
    DISPATCHER("Диспетчер", Arrays.asList("sasat", "plus", "lejat")),
    MARKET_AGENT("Торговый представитель", Arrays.asList("sasat", "plus", "lejat")),
    TEMP_REMOVED("Временно удален", Arrays.asList("sasat", "plus", "lejat")),
    W_DISPATCHER("Диспетчер склада", Arrays.asList("sasat", "plus", "lejat")),
    DRIVER("Водитель", Arrays.asList("sasat", "plus", "lejat")),
    TRANSPORT_COMPANY("Транспортная компания", Arrays.asList("sasat", "plus", "lejat"));

    @Getter
    private final String rusRoleName;

    @Getter
    private final List<String> statusesForRole;

    UserRole(String rusRoleName, List<String> statusesForRole) {
        this.rusRoleName = rusRoleName;
        this.statusesForRole = statusesForRole;
    }
}
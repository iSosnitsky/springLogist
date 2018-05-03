package sbat.logist.ru.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN("Админ", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    CLIENT_MANAGER("Клиент", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    DISPATCHER("Диспетчер", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    MARKET_AGENT("Торговый представитель", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    TEMP_REMOVED("Временно удален", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    W_DISPATCHER("Диспетчер склада", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    DRIVER("Водитель", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST")),
    TRANSPORT_COMPANY("Транспортная компания", Arrays.asList("APPROVED", "APPROVING", "ARRIVED", "DEPARTURE","READY","ERROR","RESERVED","STOP-LIST"));

    @Getter
    private final String rusRoleName;

    @Getter
    private final List<String> statusesForRole;

    UserRole(String rusRoleName, List<String> statusesForRole) {
        this.rusRoleName = rusRoleName;
        this.statusesForRole = statusesForRole;
    }
}
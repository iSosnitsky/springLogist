package sbat.logist.ru.constant;


import lombok.Getter;

public enum VehicleType {
    TENT("Тент"),
    THERMOS("Термос"),
    REFRIGERATOR("Рефрижератор");

    @Getter
    private final String name;

    VehicleType(String name) {this.name = name;}
}

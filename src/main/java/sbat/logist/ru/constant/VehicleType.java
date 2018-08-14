package sbat.logist.ru.constant;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum VehicleType {
    @JsonProperty("Тент")
    TENT("Тент"),
    @JsonProperty("Термос")
    THERMOS("Термос"),
    @JsonProperty("Рефрижератор")
    REFRIGERATOR("Рефрижератор");

    @Getter
    private final String name;

    VehicleType(String name) {this.name = name;}
}

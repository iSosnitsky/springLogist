package sbat.logist.ru.constant;


import lombok.Getter;

public enum VehicleLoadingType {
    BACK("Задняя"),
    TOP("Верхняя"),
    SIDE("Боковая");

    @Getter
    private final String vehicleLoadingType;

    VehicleLoadingType(String vehicleLoadingType) {
        this.vehicleLoadingType = vehicleLoadingType;
    }
}

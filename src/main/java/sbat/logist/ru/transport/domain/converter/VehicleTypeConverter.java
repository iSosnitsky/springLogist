package sbat.logist.ru.transport.domain.converter;

import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.VehicleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class VehicleTypeConverter implements AttributeConverter<VehicleType, String> {

    public String convertToDatabaseColumn(VehicleType value) {
        if ( value == null ) {
            return null;
        }

        return value.getName();
    }

    public VehicleType convertToEntityAttribute(String value) {
        if ( value == null ) {
            return null;
        }

        return VehicleType.valueOf(value);
    }
}
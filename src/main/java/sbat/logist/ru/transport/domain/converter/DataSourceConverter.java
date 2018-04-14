package sbat.logist.ru.transport.domain.converter;

import sbat.logist.ru.constant.DataSource;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DataSourceConverter implements AttributeConverter<DataSource, String> {

    public String convertToDatabaseColumn(DataSource value) {
        if ( value == null ) {
            return null;
        }

        return value.getDataSourceName();
    }

    public DataSource convertToEntityAttribute(String value) {
        if ( value == null ) {
            return null;
        }

        return DataSource.valueOf(value);
    }
}
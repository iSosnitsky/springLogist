package sbat.logist.ru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.transport.domain.Driver;
import sbat.logist.ru.transport.domain.TransportCompany;
import sbat.logist.ru.transport.domain.Vehicle;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastModifiedEntities {
    private List<TransportCompany> transportCompanies;
    private List<Driver> drivers;
    private List<Vehicle> vehicles;
}

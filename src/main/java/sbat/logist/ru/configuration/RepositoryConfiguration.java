package sbat.logist.ru.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import sbat.logist.ru.transport.domain.Driver;
import sbat.logist.ru.transport.domain.TransportCompany;
import sbat.logist.ru.transport.domain.Vehicle;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Driver.class);
        config.exposeIdsFor(TransportCompany.class);
        config.exposeIdsFor(Vehicle.class);

    }
}

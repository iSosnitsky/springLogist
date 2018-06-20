package sbat.logist.ru.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import sbat.logist.ru.transport.domain.*;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(RoutePoint.class);
        config.exposeIdsFor(Request.class);
        config.exposeIdsFor(Vehicle.class);
        config.exposeIdsFor(TransportCompany.class);
        config.exposeIdsFor(Driver.class);
    }

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(testConverter());

    }


    @Bean
    Converter<String, Long> testConverter() {
        return new Converter<String, Long>() {

            @Override
            public Long convert(String source) {
                //this code does _not_ get run at any point
                if (source.indexOf('/') == -1) { return Long.parseLong(source); }

                source = source.substring(source.lastIndexOf('/') + 1);
                Long id = Long.parseLong(source);

                return id;
            }
        };
    }
}
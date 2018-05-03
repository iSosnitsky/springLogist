package sbat.logist.ru.configuration;


import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        List<MediaType> supportedMediaTypes=new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new HibernateAwareObjectMapper());
        converter.setPrettyPrint(true);
        converter.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(converter);
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }


}
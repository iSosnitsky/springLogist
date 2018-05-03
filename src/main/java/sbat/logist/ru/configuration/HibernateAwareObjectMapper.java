package sbat.logist.ru.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

class HibernateAwareObjectMapper extends ObjectMapper {
    public HibernateAwareObjectMapper() {
        registerModule(new Hibernate5Module());
    }
}

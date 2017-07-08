package sbat.logist.ru.transport.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Client;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void test() {
        Optional<Client> clientOptional = clientRepository.findByClientIDExternalAndDataSource("120682", DataSource.LOGIST_1C);
        Assert.assertTrue(clientOptional.isPresent());
    }
}

package sbat.logist.ru.backup;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class ExchangeRepositoryTest {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Test
    public void test() {
        ExchangeId id = new ExchangeId();
        id.setPackageNumber(4);
        id.setServerName("KEK");

        Exchange exchange = new Exchange();
        exchange.setExchangeId(id);
        exchange.setPackageAdded(Timestamp.valueOf(LocalDateTime.now()));
        exchange.setPackageCreated(Timestamp.valueOf(LocalDateTime.now()));
        exchange.setPackageData("kek");
        exchangeRepository.save(exchange);

        final List<ExchangeId> allIds = exchangeRepository.findMaxExchangedIdGroupedByServerName();

        Assert.assertFalse(allIds.isEmpty());
        Assert.assertEquals("KEK", allIds.get(0).getServerName());
    }
}
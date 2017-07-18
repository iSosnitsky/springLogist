package sbat.logist.ru.transport.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.WatcherService;

@SpringBootTest
@Import(Application.class)
@RunWith(SpringRunner.class)
public class RouteRepositoryTest {
    @Autowired
    private RouteRepository routeRepository;
    @MockBean
    private WatcherService watcherService;

    @Test
    public void findByExternalIdAndRouteName() throws Exception {
        routeRepository.findByExternalIdAndRouteName("7125", "Арамиль-Кашино-Сысерть-Двуреченск");
    }

    @Test
    public void findByExternalIdAndDataSource() throws Exception {
        routeRepository.findByExternalIdAndDataSource("7125", DataSource.LOGIST_1C);
    }

    @Test
    public void findByRouteName() throws Exception {
    }

}
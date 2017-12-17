package sbat.logist.ru.transport.repository;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Point;

import java.util.Optional;



@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class PointRepositoryTest {
    @Autowired
    private PointRepository pointRepository;


    @Test
    public void test(){

        Optional<Point> warehousePoint = pointRepository.findByPointIdExternalAndDataSource("wle",DataSource.LOGIST_1C);

        if (warehousePoint.isPresent()){
            System.out.println(warehousePoint.toString());
        }
        Assert.assertTrue(warehousePoint.isPresent());
    }

}
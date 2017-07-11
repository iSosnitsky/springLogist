package sbat.logist.ru.parser.exchanger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import sbat.logist.ru.Application;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import(Application.class)
public class MatViewUpdaterTest {

    @Autowired
    private MatViewUpdater matViewUpdater;
    @Test
    public void execute() throws Exception {
        matViewUpdater.execute();
    }

}
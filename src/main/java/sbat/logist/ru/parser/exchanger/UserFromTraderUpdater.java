package sbat.logist.ru.parser.exchanger;

import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.parser.json.JsonTrader;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Component
public class UserFromTraderUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final int GENERATED_LENGTH = 16;
    private final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy((CharacterPredicate) codePoint ->
                    (codePoint >= 'a' && codePoint <= 'z') ||
                            (codePoint >= 'A' && codePoint <= 'Z') ||
                            (codePoint >= '0' && codePoint <= '9')
            )
            .build();

    private final UserRepository userRepository;

    @Autowired
    public UserFromTraderUpdater(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(List<JsonTrader> traders) {
        logger.info("START update users table from JSON object:[updateTrader]");
        final Map<Boolean, List<JsonTrader>> map = traders.stream()
                .collect(Collectors.partitioningBy(JsonTrader::hasValidLoginAndPassword));

        final AtomicInteger counter = new AtomicInteger(0);

        map.get(true).stream()
                .filter(trader -> !userRepository.findUserByLogin(trader.getTraderLogin()).isPresent())
                .forEach(trader -> {
                    try {
                        final String salt = randomStringGenerator.generate(GENERATED_LENGTH);
                        final String passAndSalt = md5DigestAsHex((md5DigestAsHex(trader.getTraderPassword().getBytes()) + salt).getBytes());
                        final User user = User.builder()
                                .userIDExternal(trader.getTraderId())
                                .dataSource(DataSource.LOGIST_1C)
                                .login(trader.getTraderLogin())
                                .salt(salt)
                                .passAndSalt(passAndSalt)
                                .userRole(UserRole.MARKET_AGENT)
                                .userName(trader.getTraderName())
                                .phoneNumber(trader.getTraderPhone())
                                .email(trader.getTraderEmail())
                                .position(trader.getTraderOffice())
                                .build();
                        userRepository.save(user);
                        counter.incrementAndGet();
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                });
        map.get(false).forEach(trader -> {
                    try {
                        final String uniqueLogin = generateUniqueLogin();
                        final String salt = randomStringGenerator.generate(GENERATED_LENGTH);
                        final String passAndSalt = md5DigestAsHex((md5DigestAsHex(generatePassword().getBytes()) + salt).getBytes());
                        final User user = userRepository.findByUserIDExternalAndDataSource(trader
                                .getTraderId(), DataSource.LOGIST_1C).orElseGet(() -> User.builder()
                                .userIDExternal(trader.getTraderId())
                                .dataSource(DataSource.LOGIST_1C)
                                .login(uniqueLogin)
                                .salt(salt)
                                .passAndSalt(passAndSalt)
                                .userRole(UserRole.MARKET_AGENT)
                                .userName(trader.getTraderName())
                                .phoneNumber(trader.getTraderPhone())
                                .email(trader.getTraderEmail())
                                .position(trader.getTraderOffice())
                                .build());
                        userRepository.save(user);
                        counter.incrementAndGet();
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                }
        );

        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", counter.get());
    }

    private String generateUniqueLogin() {
        String login = randomStringGenerator.generate(GENERATED_LENGTH);
        while (userRepository.findUserByLogin(login).isPresent()) {
            login = randomStringGenerator.generate(GENERATED_LENGTH);
        }
        return login;
    }

    private String generatePassword() {
        return randomStringGenerator.generate(GENERATED_LENGTH);
    }
}

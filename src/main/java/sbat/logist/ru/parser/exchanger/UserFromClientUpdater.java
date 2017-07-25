package sbat.logist.ru.parser.exchanger;

import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.parser.json.JsonClient;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.ClientRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Component
public class UserFromClientUpdater {
    public static final String CLIENT_USER_SUFFIX = "-client";
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final int GENERATED_LENGTH = 16;
    private static final String CLIENT_MANAGER = "CLIENT_MANAGER";
    private final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy((CharacterPredicate) codePoint ->
                    (codePoint >= 'a' && codePoint <= 'z') ||
                            (codePoint >= 'A' && codePoint <= 'Z') ||
                            (codePoint >= '0' && codePoint <= '9')
            )
            .build();

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public UserFromClientUpdater(
            UserRepository userRepository,
            ClientRepository clientRepository
    ) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    public void execute(List<JsonClient> clients) {
        logger.info("START update users table from JSON object:[updateClients]");
        final Map<Boolean, List<JsonClient>> map = clients.stream()
                .collect(Collectors.partitioningBy(JsonClient::hasValidPassword));

        final AtomicInteger counter = new AtomicInteger(0);

        map.get(true).stream()
                //Doesn't really check if there's no user w/ external ID = clientID+prefix
                //TODO: So fix this please
                .filter(client -> !userRepository.findUserByLogin(client.getClientId()).isPresent())
                .forEach(client -> {
                    try {
                        final String salt = randomStringGenerator.generate(GENERATED_LENGTH);
                        final String passAndSalt = md5DigestAsHex((md5DigestAsHex(client.getClientPassword().getBytes()) + salt).getBytes());
                        final Optional<Client> optional = clientRepository.findByClientIDExternal(client.getClientId());
                        final Optional<User> userOptional = userRepository.findByUserIDExternalAndDataSource(client.getClientId() + CLIENT_USER_SUFFIX, DataSource.LOGIST_1C);
                        if (optional.isPresent() && !userOptional.isPresent()) {
                            final User user = User.builder()
                                    .userIDExternal(client.getClientId() + CLIENT_USER_SUFFIX)
                                    .dataSource(DataSource.LOGIST_1C)
                                    .login(client.getClientId())
                                    .salt(salt)
                                    .passAndSalt(passAndSalt)
                                    .userRole(UserRole.CLIENT_MANAGER)
                                    .userName(client.getClientName())
                                    .client(optional.get())
                                    .build();
                            userRepository.save(user);
                            counter.incrementAndGet();
                        }
                        //SQLException
                    } catch (Exception e) {
                        logger.error("Exception thrown inserting user from client:\n {}\n Exception: \n {}",client.toString(), e.getMessage());
                    }
                });
        map.get(false)
                .forEach(client -> {
                            try {
                                final String uniqueLogin = generateUniqueLogin();
                                final String salt = randomStringGenerator.generate(GENERATED_LENGTH);
                                final String passAndSalt = md5DigestAsHex((md5DigestAsHex(generatePassword().getBytes()) + salt).getBytes());
                                final Optional<Client> optional = clientRepository.findByClientIDExternal(client.getClientId());
                                final Optional<User> userOptional = userRepository.findByUserIDExternalAndDataSource(client.getClientId() + CLIENT_USER_SUFFIX, DataSource.LOGIST_1C);
                                if (optional.isPresent() && !userOptional.isPresent()) {
                                    final User user = User.builder()
                                            .userIDExternal(client.getClientId() + CLIENT_USER_SUFFIX)
                                            .dataSource(DataSource.LOGIST_1C)
                                            .login(uniqueLogin)
                                            .salt(salt)
                                            .passAndSalt(passAndSalt)
                                            .userRole(UserRole.CLIENT_MANAGER)
                                            .userName(client.getClientName())
                                            .client(optional.get())
                                            .build();
                                    userRepository.save(user);
                                    counter.incrementAndGet();
                                }

                            } catch (Exception e) {
                                logger.error("Exception thrown inserting user from client:\n {}\n Exception: \n {}",client.toString(), e.getMessage());
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

package sbat.logist.ru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import sbat.logist.ru.configuration.property.ParserProperties;
import sbat.logist.ru.jpa.User;
import sbat.logist.ru.jpa.DataSourceRepository;
import sbat.logist.ru.jpa.UserRepository;
import sbat.logist.ru.jpa.UserRoleRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final DataSourceRepository dataSourceRepository;
    private final ParserProperties parserProperties;

    @Autowired
    public DatabaseLoader(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            DataSourceRepository dataSourceRepository,
            ParserProperties parserProperties
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.parserProperties = parserProperties;
    }

    @Override
    public void run(String... strings) throws Exception {
        final Random r = new SecureRandom();

//        Byte[] a = IntStream.generate(r::nextInt)
//                .limit(8)
//                .boxed()
//                .map(Integer::byteValue)
//                .map(Byte::byteValue)
//                .toArray(Byte[]::new);

        String password = "kek";
        final String encodedPasswordWOSalt = DigestUtils.md5DigestAsHex(password.getBytes());
        final String encodePassword = DigestUtils.md5DigestAsHex((encodedPasswordWOSalt + "salt1324").getBytes());

        final List<User> usersByUserName = userRepository.findUsersByUserName("death penus");
        if (usersByUserName.isEmpty()) {
            userRepository.save(
                    User.builder()
                            .userRole(userRoleRepository.findOne("ADMIN"))
                            .salt("salt1324")
                            .passAndSalt(encodePassword)
                            .login("eduardcowgirlovich")
                            .userName("death penus")
                            .userIDExternal("FROM_JAVA")
                            .dataSource(dataSourceRepository.findOne("ADMIN_PAGE"))
                            .phoneNumber("88005553535")
                            .email("phpsila-javamogila@mail.ru")
                            .position("The White Russian's drinker")
                            .build()
            );
        } else {
            final User user = usersByUserName.get(0);

            final String kek = DigestUtils.md5DigestAsHex((encodedPasswordWOSalt + user.getSalt()).getBytes());
            if (user.getPassAndSalt().equals(kek)) {
                System.out.println(kek);
                System.out.println(user.getPassAndSalt());
            } else {
                System.out.println(kek);
                System.out.println(user.getPassAndSalt());
            }
        }
    }
}

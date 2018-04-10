package sbat.logist.ru.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.DigestUtils;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.ClientRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class CustomAuthenticationManager implements AuthenticationManager {
    private UserRepository userRepository;
    private ClientRepository clientRepository;

    @Autowired
    public CustomAuthenticationManager(UserRepository userRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final User user;
        Optional<User> possibleUser = userRepository.findUserByLogin(authentication.getName());
        if(possibleUser.isPresent()){
            user = possibleUser.get();
        } else {
            Optional<Client>possibleClient = clientRepository.findByClientIDExternal(authentication.getName());
            if (possibleClient.isPresent()){
                possibleUser = userRepository.findByClient(possibleClient.get());
                user = possibleUser.orElseThrow(()->new BadCredentialsException("Invalid client/username or password"));
            } else {
                throw new BadCredentialsException("Invalid client/username or password");
            }
        }

//                .orElseGet(userRepository.findByClient(clientRepository.findByClientIDExternal(authentication.getName()).orElseThrow(()->new BadCredentialsException("Invalid client/username or password"))));

//                        () -> new BadCredentialsException("Invalid username or password"));
        final String encodedPassword = DigestUtils.md5DigestAsHex(authentication.getCredentials().toString().getBytes());
        final String encodedPasswordWithOldSalt = DigestUtils.md5DigestAsHex((encodedPassword + user.getSalt()).getBytes());

        Optional.of(user)
                .filter(u -> u.getUserRole() != UserRole.TEMP_REMOVED)
                .orElseThrow(() -> new DisabledException("Account is disabled"));

        if (user.getPassAndSalt().equals(encodedPasswordWithOldSalt)) {
            return new UsernamePasswordAuthenticationToken(user.getLogin(), authentication.getCredentials(), Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().name())));
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}

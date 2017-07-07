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
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class CustomAuthenticationManager implements AuthenticationManager {
    private UserRepository userRepository;

    @Autowired
    public CustomAuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final User user = userRepository.findUserByLogin(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
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

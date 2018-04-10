package sbat.logist.ru.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sbat.logist.ru.configuration.LogistUserDetails.LogistUser;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.ClientRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.*;

@Service("userDetailsService")
@Transactional
public class LogistUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadByUserLogin(String login) throws UsernameNotFoundException{
        Optional<User> user =userRepository.findUserByLogin(login);
        if (user.isPresent()){
            return new LogistUser(
                    user.get().getUserName(), user.get().getPassAndSalt(), user.get(),user.get().getUserRole()!= UserRole.TEMP_REMOVED, true, true,
                    true, getGrantedAuthorities(Collections.singletonList(user.get().getUserRole().name())));

        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}

package sbat.logist.ru.configuration.LogistUserDetails;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LogistUserDetails extends org.springframework.security.core.userdetails.User{
    private static final long serialVersionUID = -6411988532329234916L;
    private Integer personId;

    public LogistUserDetails(String username, String password, Integer personId,
                              Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.personId = personId;
    }

    public Integer getPersonId() {
        return personId;
    }
}

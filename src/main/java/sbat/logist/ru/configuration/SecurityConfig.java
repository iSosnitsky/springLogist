package sbat.logist.ru.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sbat.logist.ru.constant.UserRole;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    public SecurityConfig(CustomAuthenticationManager customAuthenticationManager) {
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/login-jquery").hasAuthority(UserRole.CLIENT_MANAGER.name())
                .and()
            .formLogin()
				.loginPage("/login").failureUrl("/login-error");
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return customAuthenticationManager;
    }
}
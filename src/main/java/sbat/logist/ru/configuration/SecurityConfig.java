package sbat.logist.ru.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sbat.logist.ru.constant.UserRole;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private CustomAuthenticationManager customAuthenticationManager;



    @Autowired
    public SecurityConfig(CustomAuthenticationManager customAuthenticationManager) {
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/index").hasRole(UserRole.ADMIN.name())
                .antMatchers("/css/**").permitAll()
                .antMatchers("/login-jquery").permitAll()
                .antMatchers("/data/**").permitAll()
                .antMatchers("/main").authenticated()
                .antMatchers("/requestHistoryPage").authenticated()
                .antMatchers("/api").permitAll()
                .and()
            .formLogin()
				.loginPage("/login")
                .failureUrl("/login-error")
                .successForwardUrl("/main");
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return customAuthenticationManager;
    }
}
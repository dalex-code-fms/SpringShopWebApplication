package fr.fms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder pe = passwordEncoder();
        auth.inMemoryAuthentication().withUser("mohamed").password(pe.encode("12345")).roles("ADMIN", "USER");
        auth.inMemoryAuthentication().withUser("david").password(pe.encode("12345")).roles("USER");
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        http.authorizeRequests().antMatchers("/index", "/save", "/delete", "/edit", "/article").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/index").hasRole("USER");
    }
}

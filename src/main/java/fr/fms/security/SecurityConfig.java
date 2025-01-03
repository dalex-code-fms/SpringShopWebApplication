package fr.fms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username = ?")
                .authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username = ?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").defaultSuccessUrl("/index");
        http.authorizeRequests().antMatchers("/logoutConfirm", "/confirm", "/porder", "/order", "/save", "/delete", "/edit", "/article").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/logoutConfirm", "/confirm", "/porder", "/order").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

        http.exceptionHandling().accessDeniedPage("/403");

        http.logout();
    }
}
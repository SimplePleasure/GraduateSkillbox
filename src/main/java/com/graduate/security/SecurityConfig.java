package com.graduate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private DataSource dataSource;
//    public SecurityConfig(@Autowired DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    private CustomUserDetailService userDetailService;
    public SecurityConfig(@Autowired CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        // TODO: 11.02.2021 implement password crypt
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("SELECT email , password , 'true' FROM blog.users where email = ?")
//                .authoritiesByUsernameQuery("SELECT email , CONCAT('ROLE_', is_moderator) FROM blog.users where email = ?");
        auth.userDetailsService(userDetailService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/test3").hasRole("1")
                .antMatchers("/test2").authenticated()
                .antMatchers("/test1").permitAll()
                .and().formLogin()
                .loginPage("/log")
                .loginProcessingUrl("/api/auth/login")
                .usernameParameter("e_mail")
                .passwordParameter("password")
                .defaultSuccessUrl("/");
    }
}
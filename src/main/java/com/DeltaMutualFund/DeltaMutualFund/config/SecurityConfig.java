package com.DeltaMutualFund.DeltaMutualFund.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{


    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/employee/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .and()
                .formLogin()
                .defaultSuccessUrl("/loginsuccess")
                .loginPage("/index")
                .failureUrl("/login-error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index")
                .and()
                .csrf().disable();
    }

//    @Configuration
//    @Order(1)
//    public static class CustomerConfigurationAdapter extends WebSecurityConfigurerAdapter {
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll()
//                    .antMatchers("/users/**").hasRole("USER")
//                    .and()
//                    .formLogin()
//                    .loginPage("/loginAsUser").defaultSuccessUrl("/account").failureUrl("/login-error")
//                    .and().exceptionHandling().accessDeniedPage("/403");
//            http.csrf().ignoringAntMatchers("/h2-console/");
//            http.headers().frameOptions().sameOrigin();
//        }
//    }
//
//    @Configuration
//    public static  class EmployeeConfigurationAdapter extends  WebSecurityConfigurerAdapter {
//        protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll()
//                .antMatchers("/employees/**").hasRole("ADMIN")
//                .and()
//                .formLogin()
//                .loginPage("/loginAsAdmin").defaultSuccessUrl("/dashboard").failureUrl("/login-error-admin")
//                .and().exceptionHandling().accessDeniedPage("/403");
//            http.csrf().ignoringAntMatchers("/h2-console/");
//            http.headers().frameOptions().sameOrigin();
//        }
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws  Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}

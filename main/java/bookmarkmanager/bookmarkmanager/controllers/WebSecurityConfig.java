package bookmarkmanager.bookmarkmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler(){
        return new AuthSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
            These pages can be public and not need auth
        */

        http.authorizeRequests().antMatchers("/", "/sortedBookmarks", "/login", "/register", "/css/**", "/js/**").permitAll();

        /*
            Use a custom login form
        */
        http.authorizeRequests().anyRequest().authenticated().and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")//
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authSuccessHandler());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource);
                /*
                Optional customised queries, e.g. if different tables are used
                 */
                /*.usersByUsernameQuery("select username, password, enabled "
                + "from users "
                + "where username =?"
                )
                .authoritiesByUsernameQuery("select username, authority "
                + "from authorities "
                + "where username = ?"
                );*/
    }
}

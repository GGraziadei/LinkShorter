package it.erroridiprezzo.ErroriDiPrezzoShort.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeHttpRequests()
                .antMatchers("/admin*").authenticated()
                .antMatchers(HttpMethod.POST , "/short").authenticated()
                .antMatchers("/").permitAll()
                .and().formLogin()
                .and().httpBasic();
        */

        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic()
                .and().formLogin();

    }

}

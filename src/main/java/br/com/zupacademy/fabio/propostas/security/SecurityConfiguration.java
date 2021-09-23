package br.com.zupacademy.fabio.propostas.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequest -> authorizeRequest
                .antMatchers(HttpMethod.GET, "/propostas/**").hasAuthority("SCOPE_propostas")
                .antMatchers(HttpMethod.POST, "/propostas/**").hasAuthority("SCOPE_propostas")
                .antMatchers(HttpMethod.POST, "/cartoes/**").hasAuthority("SCOPE_propostas")
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

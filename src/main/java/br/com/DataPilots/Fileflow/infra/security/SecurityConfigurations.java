package br.com.DataPilots.Fileflow.infra.security;

import br.com.DataPilots.Fileflow.infra.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    private SecurityFilter securityFilter;
    @Autowired
    private Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> {
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(request -> {
                request.requestMatchers(HttpMethod.POST, "/login").permitAll();
                request.requestMatchers(HttpMethod.POST, "/user").permitAll();
                request.requestMatchers(HttpMethod.POST, "/file").permitAll();
                request.requestMatchers("/password_recovery").permitAll();
                if (env.acceptsProfiles(Profiles.of("test"))) {
                    request.requestMatchers("/h2-console/**").permitAll();
                }
                request.anyRequest().authenticated();
            })


            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
            if (env.acceptsProfiles(Profiles.of("test"))) {
                http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
            }
            return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
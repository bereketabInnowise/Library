package library.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

    public SecurityConfig(ReactiveAuthenticationManager authenticationManager,
                          JwtAuthenticationWebFilter jwtAuthenticationWebFilter) {
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationWebFilter = jwtAuthenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("Gateway reactive security config is triggered");
        return http
                .csrf(csrf -> {
                    System.out.println("CSRF is being disabled");
                    csrf.disable();
                })
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/v1/login", "/api/v1/register").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
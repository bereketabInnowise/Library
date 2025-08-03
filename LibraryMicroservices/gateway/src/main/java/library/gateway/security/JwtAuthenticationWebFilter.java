package library.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        List<SimpleGrantedAuthority> authorities = jwtUtil.getRolesFromToken(token);

        if (username != null && jwtUtil.validateToken(token)) {
            UserDetails userDetails = User.withUsername(username)
                    .authorities(authorities)
                    .password("") // Password is not needed here
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            return chain.filter(exchange)
                    .contextWrite(context -> context.put(
                            org.springframework.security.core.context.SecurityContext.class,
                            new SecurityContextImpl(authentication)
                    ));
        }

        return chain.filter(exchange);
    }
}
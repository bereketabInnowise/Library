package library.gateway.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public JwtReactiveAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        try {
            // Use your existing JwtUtil to validate and extract info
            if (!jwtUtil.validateToken(token)) {
                return Mono.empty();
            }

            String username = jwtUtil.getUsernameFromToken(token);
            List<SimpleGrantedAuthority> authorities = jwtUtil.getRolesFromToken(token);

            // Create UserDetails (consistent with your existing filter)
            UserDetails userDetails = User.withUsername(username)
                    .authorities(authorities)
                    .password("") // Password not needed for JWT
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();

            // Create authenticated token
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            return Mono.just(authToken);

        } catch (Exception e) {
            // Token is invalid
            return Mono.empty();
        }
    }
}
package gazzsha.sprint.security.application.config;


import gazzsha.sprint.security.application.utils.JWTTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    JWTTokenUtils jwtTokenUtils;
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException | SignatureException exception) {
                handlerExceptionResolver.resolveException(request, response, null, exception);
            }

        }
        if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtils.getRoles(jwt).stream()
                            .map(SimpleGrantedAuthority::new).toList()

            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
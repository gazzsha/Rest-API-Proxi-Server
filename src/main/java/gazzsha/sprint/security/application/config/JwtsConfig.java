package gazzsha.sprint.security.application.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
public class JwtsConfig {
    @Bean
    public SecretKey secretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

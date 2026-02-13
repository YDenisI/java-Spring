package ru.cr.hw.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import ru.cr.hw.security.filter.MyOwnFilter;
import ru.cr.hw.services.CustomUserDetailsService;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.DELETE;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfiguration(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] authenticatedPaths = {"/", "/author", "/genre", "/book/{bookId}/comments", "/api/authors",
                "/api/genres", "/api/books", "/api/books/{id}", "/api/books/{bookId}/comments"};
        String[] adminBookPaths = {"/book/{id}", "/book/add", "/api/books", "/api/books/{id}"};
        String[] adminCommentPaths = {"/api/books/{bookId}/comments", "/api/comments/{commentId}"};
        HttpMethod[] methods = {POST, PUT, DELETE};
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(a -> {
                    a.requestMatchers(GET, authenticatedPaths).authenticated();
                    a.requestMatchers(GET, adminBookPaths).hasRole("ADMIN");
                    for (HttpMethod m : methods) {
                        a.requestMatchers(m, adminBookPaths).hasRole("ADMIN");
                        a.requestMatchers(m, adminCommentPaths).hasRole("ADMIN");
                    }
                    a.anyRequest().authenticated();
                })
                .exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        (HttpServletRequest req) -> req.getServletPath().startsWith("/api/")))
                .addFilterAfter(new MyOwnFilter(), AuthorizationFilter.class)
                .formLogin(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

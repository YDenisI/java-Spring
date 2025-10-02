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
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.cr.hw.security.filter.MyOwnFilter;
import ru.cr.hw.services.CustomUserDetailsService;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfiguration(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize

                        .requestMatchers("/").authenticated()

                        .requestMatchers(HttpMethod.GET,"/author").authenticated()

                        .requestMatchers(HttpMethod.GET,"/genre").authenticated()

                        .requestMatchers(HttpMethod.GET,"/book/{bookId}/comments").authenticated()

                        .requestMatchers(HttpMethod.GET, "/book/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/book/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/book/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/book/{id}").hasRole("ADMIN")

                        .requestMatchers("/book/add").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/authors").authenticated()

                        .requestMatchers(HttpMethod.GET,"/api/genres").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/books").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/books/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/books/{bookId}/comments").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/books/{bookId}/comments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/comments/{commentId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/comments/{commentId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/comments/{commentId}").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new RequestMatcher() {
                                    @Override
                                    public boolean matches(HttpServletRequest request) {
                                        return request.getServletPath().startsWith("/api/");
                                    }
                                }
                        )
                )
                .addFilterAfter(new MyOwnFilter(), AuthorizationFilter.class)
                .formLogin(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

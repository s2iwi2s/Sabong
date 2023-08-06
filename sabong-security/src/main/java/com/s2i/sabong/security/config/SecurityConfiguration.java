package com.s2i.sabong.security.config;

import com.s2i.sabong.security.config.jwt.JWTConfigurer;
import com.s2i.sabong.security.config.jwt.TokenProvider;
import com.s2i.sabong.service.config.AppConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "v2/api-docs",
            "/swagger-resources",
            "swagger-resources",
            "/swagger-resources/**",
            "swagger-resources/**",
            "/configuration/ui",
            "configuration/ui",
            "/configuration/security",
            "configuration/security",
            "/swagger-ui.html",
            "swagger-ui.html",
            "webjars/**",
            // -- Swagger UI v3
            "/v3/api-docs/**",
            "v3/api-docs/**",
            "/swagger-ui/**",
            "swagger-ui/**",
            // CSA Controllers
            "/csa/api/token",
            // Actuators
            "/actuator/**",
            "/health/**"
    };

    private final AppConfigProperties appConfig;

    private final TokenProvider tokenProvider;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
            AppConfigProperties appConfig,
            TokenProvider tokenProvider,
            SecurityProblemSupport problemSupport) {
        this.appConfig = appConfig;
        this.tokenProvider = tokenProvider;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("Registering Authorized Http Requests - permitAll pattern:");
        this.appConfig.getPermitAll().forEach(pattern -> log.debug("\t- {}\t- {}", "permitAll", pattern));
        this.appConfig.getAuthRequest().forEach(pattern -> log.debug("\t- {}\t- {}", "authenticated", pattern));

        http.csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.csrfTokenRepository(jwtCsrfTokenRepository)
//                        .ignoringRequestMatchers(this.appConfig.getIgnoredRequest().toArray(String[]::new)))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(AUTH_WHITELIST).permitAll()
//                        .anyRequest().authenticated()
//                )
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exh -> exh.accessDeniedHandler(problemSupport).authenticationEntryPoint(problemSupport))
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers(this.appConfig.getPermitAll().toArray(String[]::new)).permitAll()
                                .requestMatchers(this.appConfig.getAuthRequest().toArray(String[]::new)).authenticated()
//                .anyRequest().authenticated()
                ).apply(securityConfigurerAdapter());
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = appConfig.getCors();
        if (!CollectionUtils.isEmpty(config.getAllowedOrigins())) {
            log.debug("Registering CORS filter");
            for (String pattern : this.appConfig.getCorsFilter()) {
                log.debug("\t - {}", pattern);
                source.registerCorsConfiguration(pattern, config);
            }
        }
        return new CorsFilter(source);
    }
}

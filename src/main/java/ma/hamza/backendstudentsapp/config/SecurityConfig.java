package ma.hamza.backendstudentsapp.config;

import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/roles").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/roles").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/addRoleToUser").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/studentsByProgramId").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/paymentFile/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/swagger-config").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/profile").hasAnyAuthority("USER","ADMIN"))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Specify your front-end URL
        configuration.setAllowedOrigins(List.of("http://localhost:4200","http://100.25.16.179"));

        // Specify allowed methods
        configuration.setAllowedMethods(List.of("*"));

        // Specify allowed headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials
       // configuration.setAllowCredentials(true);

        // Expose headers (optional)
        configuration.setExposedHeaders(List.of("Authorization"));

        // Apply this configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}

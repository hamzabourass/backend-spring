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
import org.springframework.security.web.SecurityFilterChain;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth-> auth.requestMatchers("/api/auth/**").permitAll());
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST,"/api/payments/**").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.GET,"/api/payments/**").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.GET,"/api/students/**").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.GET,"/api/studentsByProgramId").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.GET,"/api/paymentFile/").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST,"/api/swagger-config").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST,"/api/roles").hasAuthority("ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST,"/api/addRoleToUser").hasAuthority("ADMIN"));
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080","http//localhost:4200"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }


}

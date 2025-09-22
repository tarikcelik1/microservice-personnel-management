package com.example.personal_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security yapılandırma sınıfı
 * Web güvenliği ve CORS ayarlarını yapılandırır
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * HTTP güvenlik yapılandırmasını tanımlar
     * @param http HttpSecurity nesnesi
     * @return Yapılandırılmış SecurityFilterChain
     * @throws Exception Yapılandırma hatası durumunda
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS yapılandırmasını etkinleştir
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF korumasını devre dışı bırak (API için)
            .csrf(csrf -> csrf.disable())
            // HTTP isteklerinin yetkilendirme kurallarını belirle
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()        // API endpoint'lerini herkese aç
                .requestMatchers("/swagger-ui/**").permitAll()  // Swagger UI'ye erişim
                .requestMatchers("/api-docs/**").permitAll()   // API dokümantasyonuna erişim
                .requestMatchers("/h2-console/**").permitAll() // H2 veritabanı konsoluna erişim
                .requestMatchers("/actuator/**").permitAll()   // Spring Actuator endpoint'lerine erişim
                .anyRequest().authenticated()                  // Diğer tüm istekler için kimlik doğrulama gerekli
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin()); // H2 console için frame seçeneklerini aynı origin'e izin ver

        return http.build();
    }

    /**
     * CORS (Cross-Origin Resource Sharing) yapılandırmasını sağlar
     * Frontend uygulamasının backend API'lerine erişebilmesi için gerekli
     * @return Yapılandırılmış CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Tüm origin'lerden gelen isteklere izin ver
        configuration.setAllowedOriginPatterns(List.of("*"));
        // İzin verilen HTTP method'larını belirle
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Tüm header'lara izin ver
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Credential'ların gönderilmesine izin ver
        configuration.setAllowCredentials(true);

        // URL tabanlı CORS yapılandırma kaynağı oluştur
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Tüm path'ler için bu yapılandırmayı uygula
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

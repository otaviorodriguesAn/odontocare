package senai499.com.br.odontocare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // PÁGINAS PÚBLICAS
                        .requestMatchers(
                                "/",
                                "/sobre",
                                "/servicos",
                                "/login",
                                "/cadastro"
                        ).permitAll()

                        // ARQUIVOS CSS, JS E IMAGENS
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        // DEMAIS PÁGINAS EXIGEM LOGIN
                        .anyRequest().authenticated()
                )

                // CONFIGURAÇÃO DO LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?erro")
                        .permitAll()
                )

                // CONFIGURAÇÃO DO LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
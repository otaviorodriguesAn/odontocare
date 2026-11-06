package senai499.com.br.odontocare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // REDIRECIONAMENTO DE ACORDO COM O PERFIL
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {

        return (request, response, authentication) -> {

            boolean cliente = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));

            boolean dentista = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_DENTISTA"));

            boolean admin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (cliente) {
                response.sendRedirect("/cliente");
            } else if (dentista) {
                response.sendRedirect("/dentista");
            } else if (admin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/login?erro");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler authenticationSuccessHandler)
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

                        // ARQUIVOS ESTÁTICOS
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        // ÁREA DO CLIENTE
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")

                        // ÁREA DO DENTISTA
                        .requestMatchers("/dentista/**").hasRole("DENTISTA")

                        // ÁREA DO ADMINISTRADOR
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // DEMAIS ROTAS
                        .anyRequest().authenticated()
                )

                // LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureUrl("/login?erro")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("odontocare-chave-secreta")
                        .tokenValiditySeconds(86400)
                )

                // LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
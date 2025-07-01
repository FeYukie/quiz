package com.example.quiz.app;

import com.example.quiz.app.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Define o PasswordEncoder que será usado para criptografar senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt é um algoritmo de hash de senhas seguro
    }

    // Configura o serviço que o Spring Security usará para carregar detalhes do usuário
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioService.findByUsername(username)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // Configura o provedor de autenticação (como o Spring Security encontrará e verificará usuários)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Configura as regras de segurança HTTP (quais URLs são protegidas, formulários de login, etc.)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // URLs públicas (acessíveis sem login)
                .requestMatchers("/", "/css/**", "/js/**", "/login", "/cadastro").permitAll()
                // URL para criar quiz requer o papel de ADMIN. Mude para USER ou remova se todos puderem criar.
                .requestMatchers("/quizes/novo").hasRole("ADMIN")
                // Todas as outras requisições requerem autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Define a URL da sua página de login personalizada
                .defaultSuccessUrl("/", true) // Redireciona para a página inicial após login bem-sucedido
                .permitAll() // Permite acesso à página de login para todos
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para deslogar
                .logoutSuccessUrl("/login?logout") // Redireciona para a página de login com mensagem de logout
                .permitAll()
            );
        return http.build();
    }
}

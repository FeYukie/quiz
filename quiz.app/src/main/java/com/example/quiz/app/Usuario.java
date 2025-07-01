package com.example.quiz.app;

import jakarta.persistence.*; // Certifique-se que o import é jakarta
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class Usuario implements UserDetails { // Implementa UserDetails para o Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Usado para login (nome de usuário)

    @Column(nullable = false)
    private String password; // Senha (hashada)

    @Enumerated(EnumType.STRING) // Armazena a enum como String no DB
    private Role role; // Papel do usuário (ex: USER, ADMIN)

    public Usuario() {
    }

    public Usuario(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Métodos da interface UserDetails
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Métodos adicionais da interface UserDetails para controle de conta
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a lista de permissões/roles para este usuário
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta não está expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais não estão expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está habilitada
    }
}

package com.example.quiz.app;

import com.example.quiz.app.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data JPA vai implementar este método automaticamente
    Optional<Usuario> findByUsername(String username);
}

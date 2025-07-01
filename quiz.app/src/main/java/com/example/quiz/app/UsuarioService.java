package com.example.quiz.app;

import com.example.quiz.app.Role;
import com.example.quiz.app.Usuario;
import com.example.quiz.app.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Para criptografar senhas
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Injeta o encoder de senhas

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        // Opcional: Criar um usuário admin padrão ao iniciar a aplicação se não houver nenhum
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("adminpass"), Role.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuário 'admin' criado com senha 'adminpass'");
        }
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario registrarNovoUsuario(String username, String password) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já existe!");
        }
        Usuario novoUsuario = new Usuario(username, passwordEncoder.encode(password), Role.USER); // Criptografa a senha
        return usuarioRepository.save(novoUsuario);
    }
}

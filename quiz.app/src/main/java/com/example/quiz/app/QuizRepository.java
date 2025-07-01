package com.example.quiz.app;

import com.example.quiz.app.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta interface é um repositório Spring Data JPA
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Spring Data JPA fornece automaticamente métodos CRUD (save, findById, findAll, delete, etc.)
    // Podemos adicionar métodos personalizados aqui se precisarmos de queries específicas, por exemplo:
    // List<Quiz> findByTituloContaining(String titulo);
}

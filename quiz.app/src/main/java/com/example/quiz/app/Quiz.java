package com.example.quiz.app;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA (mapeia para uma tabela no DB)
public class Quiz {

    @Id // Marca o campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    private String titulo;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true) // Um Quiz tem muitas Perguntas
    private List<Pergunta> perguntas = new ArrayList<>();

    // Construtores
    public Quiz() {
    }

    public Quiz(String titulo) {
        this.titulo = titulo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    // Método para adicionar pergunta e garantir a ligação bidirecional
    public void addPergunta(Pergunta pergunta) {
        perguntas.add(pergunta);
        pergunta.setQuiz(this);
    }

    // Método para remover pergunta
    public void removePergunta(Pergunta pergunta) {
        perguntas.remove(pergunta);
        pergunta.setQuiz(null);
    }
}

package com.example.quizapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String textoPergunta;

    @ElementCollection // Mapeia uma coleção de elementos simples (String)
    @CollectionTable(name = "alternativa", joinColumns = @JoinColumn(name = "pergunta_id"))
    @Column(name = "alternativa_texto")
    private List<String> alternativas = new ArrayList<>();

    private int indiceAlternativaCorreta; // 0-based

    @ManyToOne(fetch = FetchType.LAZY) // Muitas Perguntas pertencem a um Quiz
    @JoinColumn(name = "quiz_id") // Coluna que armazena a chave estrangeira para Quiz
    private Quiz quiz;

    // Construtores
    public Pergunta() {
    }

    public Pergunta(String textoPergunta, List<String> alternativas, int indiceAlternativaCorreta) {
        this.textoPergunta = textoPergunta;
        this.alternativas = alternativas;
        this.indiceAlternativaCorreta = indiceAlternativaCorreta;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextoLancado() { // Renomeado para seguir o nome do getter anterior
        return textoPergunta;
    }

    public void setTextoLancado(String textoPergunta) {
        this.textoPergunta = textoPergunta;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativas = alternativas;
    }

    public int getIndiceAlternativaCorreta() {
        return indiceAlternativaCorreta;
    }

    public void setIndiceAlternativaCorreta(int indiceAlternativaCorreta) {
        this.indiceAlternativaCorreta = indiceAlternativaCorreta;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

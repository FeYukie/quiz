package com.example.quiz.app;

import java.util.List;

public class Pergunta {
    private String id; // Para identificar a pergunta, ex: "q1", "q2"
    private String textoPergunta;
    private List<String> alternativas;
    private int indiceAlternativaCorreta; // 0-based

    // Construtor
    public Pergunta(String id, String textoPergunta, List<String> alternativas, int indiceAlternativaCorreta) {
        this.id = id;
        this.textoPergunta = textoPergunta;
        this.alternativas = alternativas;
        this.indiceAlternativaCorreta = indiceAlternativaCorreta;
    }

    // Getters (métodos para acessar os atributos)
    public String getId() {
        return id;
    }

    public String getTextoLancado() {
        return textoPergunta;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public int getIndiceAlternativaCorreta() {
        return indiceAlternativaCorreta;
    }

    // Setters (métodos para modificar os atributos - nem sempre necessários para POJOs simples)
    public void setId(String id) {
        this.id = id;
    }

    public void setTextoLancado(String textoPergunta) {
        this.textoPergunta = textoPergunta;
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativas = alternativas;
    }

    public void setIndiceAlternativaCorreta(int indiceAlternativaCorreta) {
        this.indiceAlternativaCorreta = indiceAlternativaCorreta;
    }
}
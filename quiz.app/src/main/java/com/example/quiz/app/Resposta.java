package com.example.quiz.app;

public class Resposta {
    private String perguntaId;
    private int alternativaEscolhida; // 1-based (como o usuário veria na tela)

    // Construtor vazio (necessário para Spring poder "deserializar" o JSON)
    public Resposta() {
    }

    // Construtor com parâmetros
    public Resposta(String perguntaId, int alternativaEscolhida) {
        this.perguntaId = perguntaId;
        this.alternativaEscolhida = alternativaEscolhida;
    }

    // Getters e Setters
    public String getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(String perguntaId) {
        this.perguntaId = perguntaId;
    }

    public int getAlternativaEscolhida() {
        return alternativaEscolhida;
    }

    public void setAlternativaEscolhida(int alternativaEscolhida) {
        this.alternativaEscolhida = alternativaEscolhida;
    }
}
package com.example.quiz.app;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta classe é um "Serviço" no Spring
public class QuizService {

    // Lista de perguntas hardcoded (em um projeto real, viriam de um banco de dados)
    private final List<Pergunta> perguntas = new ArrayList<>();

    public QuizService() {
        // Inicializa as perguntas ao criar o serviço
        perguntas.add(new Pergunta(
                "q1",
                "Qual a capital da França?",
                Arrays.asList("Paris", "Londres", "Berlim", "Roma"),
                0 // Paris
        ));
        perguntas.add(new Pergunta(
                "q2",
                "Qual é o planeta mais próximo do Sol?",
                Arrays.asList("Mercúrio", "Vênus", "Marte", "Júpiter"),
                0 // Mercúrio
        ));
        perguntas.add(new Pergunta(
                "q3",
                "Qual linguagem de programação é frequentemente usada para desenvolvimento web backend e mobile (Android)?",
                Arrays.asList("Java", "Python", "C++", "JavaScript"),
                0 // Java
        ));
        perguntas.add(new Pergunta(
                "q4",
                "Quem pintou a Mona Lisa?",
                Arrays.asList("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet"),
                2 // Leonardo da Vinci
        ));
    }

    public List<Pergunta> getTodasAsPerguntas() {
        return perguntas;
    }

    public Optional<Pergunta> getPerguntaPorId(String id) {
        return perguntas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public boolean verificarResposta(String perguntaId, int alternativaEscolhida) {
        Optional<Pergunta> perguntaOptional = getPerguntaPorId(perguntaId);
        if (perguntaOptional.isPresent()) {
            Pergunta pergunta = perguntaOptional.get();
            // Lembre-se que alternativaEscolhida vem 1-based (do HTML), e o índice é 0-based
            return (alternativaEscolhida - 1) == pergunta.getIndiceAlternativaCorreta();
        }
        return false; // Pergunta não encontrada
    }
}
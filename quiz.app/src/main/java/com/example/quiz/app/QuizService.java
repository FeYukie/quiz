package com.example.quiz.app;

import com.example.quiz.app.Pergunta;
import com.example.quiz.app.Quiz;
import com.example.quiz.app.PerguntaRepository;
import com.example.quiz.app.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Importe para caso de mock data
import java.util.Arrays; // Importe para caso de mock data
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final PerguntaRepository perguntaRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, PerguntaRepository perguntaRepository) {
        this.quizRepository = quizRepository;
        this.perguntaRepository = perguntaRepository;

        // Opcional: Popular o DB com um quiz inicial se ele estiver vazio
        // Isso é útil para ter dados ao iniciar a aplicação pela primeira vez
        if (quizRepository.count() == 0) { // Verifica se não há quizzes
            seedDatabase();
        }
    }

    // Método para popular o banco de dados com dados iniciais (seed)
    private void seedDatabase() {
        Quiz quiz = new Quiz("Quiz de Conhecimentos Gerais");

        Pergunta p1 = new Pergunta(
                "Qual a capital da França?",
                Arrays.asList("Paris", "Londres", "Berlim", "Roma"),
                0
        );
        Pergunta p2 = new Pergunta(
                "Qual é o planeta mais próximo do Sol?",
                Arrays.asList("Mercúrio", "Vênus", "Marte", "Júpiter"),
                0
        );
        Pergunta p3 = new Pergunta(
                "Qual linguagem de programação é frequentemente usada para desenvolvimento web backend e mobile (Android)?",
                Arrays.asList("Java", "Python", "C++", "JavaScript"),
                0
        );
        Pergunta p4 = new Pergunta(
                "Quem pintou a Mona Lisa?",
                Arrays.asList("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet"),
                2
        );

        quiz.addPergunta(p1);
        quiz.addPergunta(p2);
        quiz.addPergunta(p3);
        quiz.addPergunta(p4);

        quizRepository.save(quiz); // Salva o quiz e suas perguntas no banco de dados
    }

    public Quiz criarNovoQuiz(Quiz quiz) {
        // Garantir que as perguntas estejam ligadas ao quiz antes de salvar
        quiz.getPerguntas().forEach(pergunta -> pergunta.setQuiz(quiz));
        return quizRepository.save(quiz);
    }

    public List<Quiz> getTodosOsQuizes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizPorId(Long id) {
        return quizRepository.findById(id);
    }

    public Optional<Pergunta> getPerguntaDoQuizPorIndice(Long quizId, int indicePergunta) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            if (indicePergunta >= 0 && indicePergunta < quiz.getPerguntas().size()) {
                return Optional.of(quiz.getPerguntas().get(indicePergunta));
            }
        }
        return Optional.empty();
    }

    public boolean verificarResposta(Long perguntaId, int alternativaEscolhida) {
        Optional<Pergunta> perguntaOptional = perguntaRepository.findById(perguntaId);
        if (perguntaOptional.isPresent()) {
            Pergunta pergunta = perguntaOptional.get();
            return (alternativaEscolhida - 1) == pergunta.getIndiceAlternativaCorreta();
        }
        return false;
    }
}

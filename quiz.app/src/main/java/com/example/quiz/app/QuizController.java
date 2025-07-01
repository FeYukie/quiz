package com.example.quiz.app;

import com.example.quiz.app.Pergunta;
import com.example.quiz.app.Quiz;
import com.example.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Endpoint para a página inicial (lista de quizzes)
    // Acessar via: GET http://localhost:8080/
    @GetMapping("/")
    public String listarQuizes(Model model) {
        List<Quiz> quizes = quizService.getTodosOsQuizes();
        model.addAttribute("quizes", quizes);
        return "index"; // Novo template: index.html
    }

    // Endpoint para exibir a página de criação de quiz
    // Acessar via: GET http://localhost:8080/quizes/novo
    @GetMapping("/quizes/novo")
    public String showCriarQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        // Adiciona uma pergunta vazia inicial para o formulário
        model.addAttribute("pergunta", new Pergunta());
        return "criar-quiz"; // Novo template: criar-quiz.html
    }

    // Endpoint para processar o formulário de criação de quiz
    // Acessar via: POST de um formulário em /quizes/novo
    @PostMapping("/quizes/novo")
    public String criarQuiz(@ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
        // As perguntas virão do formulário, mas precisamos garantir que cada pergunta
        // tenha o quiz associado e que as alternativas estejam populadas corretamente.
        // O Thymeleaf e Spring MVC já fazem um bom trabalho em ligar os campos do form ao objeto Quiz.

        // Em um cenário real, você faria validações aqui.
        quizService.criarNovoQuiz(quiz); // Salva o quiz e suas perguntas

        redirectAttributes.addFlashAttribute("message", "Quiz criado com sucesso!");
        return "redirect:/"; // Redireciona para a lista de quizzes
    }


    // Endpoint para iniciar um quiz específico
    // Acessar via: GET http://localhost:8080/quiz/{quizId}
    @GetMapping("/quiz/{quizId}")
    public String iniciarQuizEspecifico(@PathVariable Long quizId, Model model) {
        return "redirect:/quiz/" + quizId + "?questionIndex=0&score=0";
    }

    // Endpoint para exibir as perguntas de um quiz específico
    // Acessar via: GET http://localhost:8080/quiz/{quizId}?questionIndex=X&score=Y
    @GetMapping("/quiz/{quizId}/pergunta")
    public String exibirPergunta(
            @PathVariable Long quizId,
            @RequestParam(name = "questionIndex") int questionIndex,
            @RequestParam(name = "score") int score,
            @RequestParam(name = "message", required = false) String message,
            Model model
    ) {
        Optional<Quiz> quizOptional = quizService.getQuizPorId(quizId);
        if (quizOptional.isEmpty()) {
            // Quiz não encontrado, redireciona para a página inicial com erro
            return "redirect:/?error=QuizNaoEncontrado";
        }

        Quiz quiz = quizOptional.get();
        List<Pergunta> perguntasDoQuiz = quiz.getPerguntas();

        if (questionIndex >= 0 && questionIndex < perguntasDoQuiz.size()) {
            Pergunta perguntaAtual = perguntasDoQuiz.get(questionIndex);
            model.addAttribute("quizId", quizId); // Passa o ID do quiz
            model.addAttribute("pergunta", perguntaAtual);
            model.addAttribute("questionIndex", questionIndex);
            model.addAttribute("score", score);
            if (message != null) {
                model.addAttribute("message", message);
            }
            return "quiz"; // Retorna o template quiz.html
        } else {
            // Fim do quiz, redireciona para a tela de resultado
            return "redirect:/resultado?score=" + score + "&total=" + perguntasDoQuiz.size();
        }
    }

    // Endpoint para processar a resposta do usuário para um quiz específico
    // Acessar via: POST de um formulário em /quiz/responder
    @PostMapping("/quiz/responder")
    public String processarResposta(
            @RequestParam(name = "quizId") Long quizId, // Recebe o ID do quiz
            @RequestParam(name = "perguntaId") Long perguntaId, // O ID da pergunta atual
            @RequestParam(name = "userAnswer") int userAnswer,
            @RequestParam(name = "questionIndex") int currentQuestionIndex,
            @RequestParam(name = "score") int currentScore
    ) {
        String message = null;
        if (quizService.verificarResposta(perguntaId, userAnswer)) {
            currentScore++;
            message = "Correto!";
        } else {
            Optional<Pergunta> p = quizService.getPerguntaDoQuizPorIndice(quizId, currentQuestionIndex); // Busca a pergunta pelo ID
            if(p.isPresent()){
                message = "Incorreto. A resposta correta era: " + (p.get().getIndiceAlternativaCorreta() + 1);
            } else {
                message = "Incorreto.";
            }
        }
        return "redirect:/quiz/" + quizId + "/pergunta?questionIndex=" + (currentQuestionIndex + 1) + "&score=" + currentScore + "&message=" + message;
    }

    // Endpoint para exibir o resultado final 
    @GetMapping("/resultado")
    public String exibirResultado(
            @RequestParam(name = "score") int score,
            @RequestParam(name = "total") int totalPerguntas,
            Model model
    ) {
        model.addAttribute("score", score);
        model.addAttribute("totalPerguntas", totalPerguntas);
        return "resultado";
    }
}

package com.example.quiz.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Mudança aqui para @Controller
import org.springframework.ui.Model; // Para passar dados do Java para o HTML
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller // Usamos @Controller para servir páginas web
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Endpoint para iniciar ou reiniciar o quiz
    // Acessar via: GET http://localhost:8080/
    @GetMapping("/")
    public String iniciarQuiz(Model model) {
        // Redireciona para a primeira pergunta (índice 0) com pontuação 0
        return "redirect:/quiz?questionIndex=0&score=0";
    }

    // Endpoint para exibir as perguntas do quiz
    // Acessar via: GET http://localhost:8080/quiz?questionIndex=X&score=Y
    @GetMapping("/quiz")
    public String exibirPergunta(
            @RequestParam(name = "questionIndex") int questionIndex,
            @RequestParam(name = "score") int score,
            @RequestParam(name = "message", required = false) String message, // Mensagem opcional
            Model model
    ) {
        // Obtém todas as perguntas do serviço
        List<Pergunta> todasAsPerguntas = quizService.getTodasAsPerguntas();

        // Verifica se ainda há perguntas a serem exibidas
        if (questionIndex < todasAsPerguntas.size()) {
            Pergunta perguntaAtual = todasAsPerguntas.get(questionIndex);
            model.addAttribute("pergunta", perguntaAtual); // Passa a pergunta para o HTML
            model.addAttribute("questionIndex", questionIndex); // Passa o índice da pergunta para o HTML
            model.addAttribute("score", score); // Passa a pontuação atual para o HTML
            if (message != null) {
                model.addAttribute("message", message); // Passa a mensagem para o HTML
            }
            return "quiz"; // Retorna o nome do template HTML (quiz.html)
        } else {
            // Se não há mais perguntas, redireciona para a tela de resultado
            return "redirect:/resultado?score=" + score + "&total=" + todasAsPerguntas.size();
        }
    }

    // Endpoint para processar a resposta do usuário
    // Acessar via: POST de um formulário no /quiz
    @PostMapping("/responder")
    public String processarResposta(
            @RequestParam(name = "perguntaId") String perguntaId,
            @RequestParam(name = "userAnswer") int userAnswer,
            @RequestParam(name = "questionIndex") int currentQuestionIndex,
            @RequestParam(name = "score") int currentScore
    ) {
        String message = null;
        // Verifica se a resposta está correta
        if (quizService.verificarResposta(perguntaId, userAnswer)) {
            currentScore++; // Incrementa a pontuação se a resposta for correta
            message = "Correto!";
        } else {
            // Se incorreto, podemos adicionar uma mensagem mais específica
            Optional<Pergunta> p = quizService.getPerguntaPorId(perguntaId);
            if(p.isPresent()){
                message = "Incorreto. A resposta correta era: " + (p.get().getIndiceAlternativaCorreta() + 1);
            } else {
                message = "Incorreto.";
            }
        }

        // Redireciona para a próxima pergunta (ou tela de resultado)
        // Passa o novo índice da pergunta, a pontuação atual e a mensagem como parâmetros de URL
        return "redirect:/quiz?questionIndex=" + (currentQuestionIndex + 1) + "&score=" + currentScore + "&message=" + message;
    }

    // Endpoint para exibir o resultado final
    // Acessar via: GET http://localhost:8080/resultado?score=X&total=Y
    @GetMapping("/resultado")
    public String exibirResultado(
            @RequestParam(name = "score") int score,
            @RequestParam(name = "total") int totalPerguntas,
            Model model
    ) {
        model.addAttribute("score", score); // Passa a pontuação final para o HTML
        model.addAttribute("totalPerguntas", totalPerguntas); // Passa o total de perguntas para o HTML
        return "resultado"; // Retorna o nome do template HTML (resultado.html)
    }

    // Você pode remover os endpoints /hello e /quiz-info se não precisar mais deles.
    // Ou mantê-los se quiser testar a API REST separadamente.
    @GetMapping("/hello")
    public String hello() {
        return "Olá do Spring Boot!";
    }
}
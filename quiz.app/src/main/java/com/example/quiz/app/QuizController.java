package com.example.quiz.app;

import com.example.quiz.app.*; // Importa todas as classes do pacote model
import com.example.quiz.app.QuizService;
import com.example.quiz.app.UsuarioService; // Importa o novo serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class QuizController {

    private final QuizService quizService;
    private final UsuarioService usuarioService; // Injeta o serviço de usuário

    @Autowired
    public QuizController(QuizService quizService, UsuarioService usuarioService) {
        this.quizService = quizService;
        this.usuarioService = usuarioService;
    }

    // --- Endpoints de Autenticação/Cadastro ---

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Retorna o template login.html
    }

    @GetMapping("/cadastro")
    public String showCadastroForm(Model model) {
        model.addAttribute("usuario", new Usuario()); // Objeto para o formulário Thymeleaf
        return "cadastro"; // Retorna o template cadastro.html
    }

    @PostMapping("/cadastro")
    public String registerUser(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrarNovoUsuario(usuario.getUsername(), usuario.getPassword());
            redirectAttributes.addFlashAttribute("message", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    // --- Endpoints do Quiz (já existentes, com possíveis ajustes) ---

    // Endpoint para a página inicial (lista de quizzes)
    // Acessar via: GET http://localhost:8080/
    @GetMapping("/")
    public String listarQuizes(Model model) {
        List<Quiz> quizes = quizService.getTodosOsQuizes();
        model.addAttribute("quizes", quizes);
        return "index"; // Template: index.html
    }

    // Endpoint para exibir a página de criação de quiz
    // Acessar via: GET http://localhost:8080/quizes/novo
    @GetMapping("/quizes/novo")
    public String showCriarQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("pergunta", new Pergunta()); // Objeto para o formulário de perguntas
        return "criar-quiz"; // Template: criar-quiz.html
    }

    // Endpoint para processar o formulário de criação de quiz
    @PostMapping("/quizes/novo")
    public String criarQuiz(@ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
        quizService.criarNovoQuiz(quiz);
        redirectAttributes.addFlashAttribute("message", "Quiz criado com sucesso!");
        return "redirect:/"; // Redireciona para a lista de quizzes
    }

    // Endpoint para iniciar um quiz específico
    @GetMapping("/quiz/{quizId}")
    public String iniciarQuizEspecifico(@PathVariable Long quizId, Model model) {
        return "redirect:/quiz/" + quizId + "/pergunta?questionIndex=0&score=0";
    }

    // Endpoint para exibir as perguntas de um quiz específico
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
            return "redirect:/?error=QuizNaoEncontrado";
        }

        Quiz quiz = quizOptional.get();
        List<Pergunta> perguntasDoQuiz = quiz.getPerguntas();

        if (questionIndex >= 0 && questionIndex < perguntasDoQuiz.size()) {
            Pergunta perguntaAtual = perguntasDoQuiz.get(questionIndex);
            model.addAttribute("quizId", quizId);
            model.addAttribute("pergunta", perguntaAtual);
            model.addAttribute("questionIndex", questionIndex);
            model.addAttribute("score", score);
            if (message != null) {
                model.addAttribute("message", message);
            }
            return "quiz";
        } else {
            return "redirect:/resultado?score=" + score + "&total=" + perguntasDoQuiz.size();
        }
    }

    // Endpoint para processar a resposta do usuário para um quiz específico
    @PostMapping("/quiz/responder")
    public String processarResposta(
            @RequestParam(name = "quizId") Long quizId,
            @RequestParam(name = "perguntaId") Long perguntaId,
            @RequestParam(name = "userAnswer") int userAnswer,
            @RequestParam(name = "questionIndex") int currentQuestionIndex,
            @RequestParam(name = "score") int currentScore
    ) {
        String message = null;
        if (quizService.verificarResposta(perguntaId, userAnswer)) {
            currentScore++;
            message = "Correto!";
        } else {
            Optional<Pergunta> p = quizService.getPerguntaDoQuizPorIndice(quizId, currentQuestionIndex);
            if(p.isPresent()){
                // O índice da resposta correta é 0-based, mas a exibição no HTML é 1-based
                message = "Incorreto. A resposta correta era: " + (p.get().getIndiceAlternativaCorreta() + 1);
            } else {
                message = "Incorreto."; // Caso a pergunta não seja encontrada (improvável)
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

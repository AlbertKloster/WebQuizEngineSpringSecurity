package engine.controller;

import engine.dto.*;
import engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizIdTitleTextOptionsDTO> postQuiz(@Valid @RequestBody QuizTitleTextOptionsAnswerDTO quizTitleTextOptionsAnswerDTO, Principal principal) {
        return quizService.addQuiz(quizTitleTextOptionsAnswerDTO, principal.getName());
    }

    @GetMapping("{id}")
    public ResponseEntity<QuizIdTitleTextOptionsDTO> getById(@PathVariable long id) {
        return quizService.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<QuizIdTitleTextOptionsDTO>> getAll(@RequestParam int page) {
        return quizService.findAll(page);
    }

    @PostMapping("{id}/solve")
    public ResponseEntity<ResponseSuccessFeedbackDTO> postResponse(@PathVariable long id, @RequestBody AnswerDTO answerDTO, Principal principal) {
        return quizService.solveById(id, answerDTO, principal.getName());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable long id, Principal principal) {
        return quizService.deleteById(id, principal.getName());
    }

    @PutMapping("{id}")
    public ResponseEntity<QuizIdTitleTextOptionsDTO> updateQuiz(@PathVariable long id, @Valid @RequestBody QuizTitleTextOptionsAnswerDTO quizTitleTextOptionsAnswerDTO, Principal principal) {
        return quizService.updateQuiz(id, quizTitleTextOptionsAnswerDTO, principal.getName());
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<QuizIdCompletedAtDTO>> getAllCompletedByUser(@RequestParam int page, Principal principal) {
        return quizService.findAllCompletedByUser(principal.getName(), page);
    }

}

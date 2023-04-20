package engine.service;

import engine.dto.*;
import engine.entity.Completed;
import engine.entity.Quiz;
import engine.repository.CompletedRepository;
import engine.repository.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CompletedRepository completedRepository;

    public QuizService(QuizRepository quizRepository, CompletedRepository completedRepository) {
        this.quizRepository = quizRepository;
        this.completedRepository = completedRepository;
    }

    public ResponseEntity<QuizIdTitleTextOptionsDTO> addQuiz(QuizTitleTextOptionsAnswerDTO quizTitleTextOptionsAnswerDTO, String email) {
        return new ResponseEntity<>(parseQuizToIdTitleTextOptionsDTO(quizRepository.save(parseTitleTextOptionsAnswerDtoToQuiz(quizTitleTextOptionsAnswerDTO, email))), HttpStatus.OK);
    }

    public ResponseEntity<QuizIdTitleTextOptionsDTO> findById(long id) {
        return quizRepository.findById(id)
                .map(this::parseQuizToIdTitleTextOptionsDTO)
                .map(quizIdTitleTextOptions -> new ResponseEntity<>(quizIdTitleTextOptions, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Page<QuizIdTitleTextOptionsDTO>> findAll(int page) {
        return new ResponseEntity<>(quizRepository.findAll(PageRequest.of(page, 10)).map(this::parseQuizToIdTitleTextOptionsDTO), HttpStatus.OK);
    }

    public ResponseEntity<Page<QuizIdCompletedAtDTO>> findAllCompletedByUser(String email, int page) {
        return new ResponseEntity<>(completedRepository.findByEmailOrderByCompletedAtDesc(email, PageRequest.of(page, 10)).map(this::parseCompletedToIdCompletedAtDTO), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSuccessFeedbackDTO> solveById(long id, AnswerDTO answerDTO, String email) {
        List<Integer> answer = answerDTO == null ? null : answerDTO.answer();
        Optional<Quiz> byId = quizRepository.findById(id);
        if (byId.isPresent()) {
            boolean success = isSuccess(byId.get().getAnswer(), answer);
            if (success) {
                completedRepository.save(new Completed(email, byId.get().getId()));
                return new ResponseEntity<>(new ResponseSuccessFeedbackDTO(true, "Congratulations, you're right!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseSuccessFeedbackDTO(false, "Wrong answer! Please, try again."), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Void> deleteById(long id, String email) {
        Optional<Quiz> byId = quizRepository.findById(id);
        if (byId.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!byId.get().getEmail().equals(email.toLowerCase())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        quizRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isSuccess(List<Integer> rightAnswer, List<Integer> answer) {
        if (rightAnswer == null && answer.isEmpty()) return true;
        if (rightAnswer == null) return false;
        if (rightAnswer.isEmpty() && answer.isEmpty()) return true;
        if (rightAnswer.size() != answer.size()) return false;
        return new HashSet<>(answer).containsAll(rightAnswer);
    }

    private QuizIdTitleTextOptionsDTO parseQuizToIdTitleTextOptionsDTO(Quiz quiz) {
        return new QuizIdTitleTextOptionsDTO(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    private Quiz parseTitleTextOptionsAnswerDtoToQuiz(QuizTitleTextOptionsAnswerDTO quizTitleTextOptionsAnswerDTO, String email) {
        return new Quiz(quizTitleTextOptionsAnswerDTO.title(), quizTitleTextOptionsAnswerDTO.text(), quizTitleTextOptionsAnswerDTO.options(), quizTitleTextOptionsAnswerDTO.answer(), email);
    }

    private QuizIdCompletedAtDTO parseCompletedToIdCompletedAtDTO(Completed completed) {
        return new QuizIdCompletedAtDTO(completed.getQuizId(), completed.getCompletedAt());
    }

    public ResponseEntity<QuizIdTitleTextOptionsDTO> updateQuiz(long id, QuizTitleTextOptionsAnswerDTO quizTitleTextOptionsAnswerDTO, String email) {
        Optional<Quiz> byId = quizRepository.findById(id);
        if (byId.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!byId.get().getEmail().equals(email.toLowerCase())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Quiz quiz = byId.get();
        quiz.setTitle(quizTitleTextOptionsAnswerDTO.title());
        quiz.setText(quizTitleTextOptionsAnswerDTO.text());
        quiz.setOptions(quizTitleTextOptionsAnswerDTO.options());
        quiz.setAnswer(quizTitleTextOptionsAnswerDTO.answer());
        quizRepository.save(quiz);
        return new ResponseEntity<>(new QuizIdTitleTextOptionsDTO(id, quiz.getTitle(), quiz.getText(), quiz.getOptions()), HttpStatus.OK);
    }
}

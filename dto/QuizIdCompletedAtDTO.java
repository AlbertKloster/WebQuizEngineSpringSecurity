package engine.dto;

import java.time.LocalDateTime;

public record QuizIdCompletedAtDTO(long id, LocalDateTime completedAt) {
}

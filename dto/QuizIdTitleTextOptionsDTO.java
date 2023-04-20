package engine.dto;

import java.util.List;

public record QuizIdTitleTextOptionsDTO(long id, String title, String text, List<String> options) {
}

package engine.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String text;
    @ElementCollection
    private List<String> options;
    @ElementCollection
    private List<Integer> answer;
    private String email;

    public Quiz() {
    }

    public Quiz(String title, String text, List<String> options, List<Integer> answer, String email) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

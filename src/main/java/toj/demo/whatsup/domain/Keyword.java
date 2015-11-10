package toj.demo.whatsup.domain;

import javax.persistence.*;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Entity(name="Keywords")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @Column(unique = true)
    private String text;

    public Keyword(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getWordId() {
        return wordId;
    }

    public Keyword(){

    }

    @Override
    public String toString() {
        return "Keyword{" +
                "wordId=" + wordId +
                ", text='" + text + '\'' +
                '}';
    }
}

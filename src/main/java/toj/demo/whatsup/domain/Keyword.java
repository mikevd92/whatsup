package toj.demo.whatsup.domain;

import javax.persistence.*;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Entity(name="Keywords")
public class Keyword {

    @Id
    @GeneratedValue
    private Long wordId;

    @Column(unique = true)
    private String content;

    public Keyword(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Long getWordId() {
        return wordId;
    }


}

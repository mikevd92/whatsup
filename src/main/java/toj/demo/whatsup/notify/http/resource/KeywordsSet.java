package toj.demo.whatsup.notify.http.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@XmlRootElement
public class KeywordsSet {

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    private Set<String> keywords;

    public KeywordsSet(){

    }

    public KeywordsSet(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }



}

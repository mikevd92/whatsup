package toj.demo.whatsup.user.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class SessionResponse implements Serializable{

    private List<Session> results;

    public SessionResponse(){

    }

    public SessionResponse(Session session) {
        this.results = Collections.singletonList(session);
    }

    public List<Session> getResults() {
        return results;
    }

    public void setResults(List<Session> results) {
        this.results = results;
    }
    @Override
    public String toString() {
        return "{\"results\":\"" + results + "\"}";
    }
}

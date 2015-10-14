package toj.demo.whatsup.user.http.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class SessionResponse implements Serializable {

    private List<SessionDTO> results;

    public SessionResponse() {

    }

    public SessionResponse(SessionDTO session) {
        this.results = Collections.singletonList(session);
    }

    public List<SessionDTO> getResults() {
        return results;
    }


    @Override
    public String toString() {
        return "{\"results\":\"" + results + "\"}";
    }
}

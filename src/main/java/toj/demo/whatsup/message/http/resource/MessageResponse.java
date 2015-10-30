package toj.demo.whatsup.message.http.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class MessageResponse {
    private List<MessageDTO> results;

    public MessageResponse() {
    }

    public MessageResponse(List<MessageDTO> results) {
        this.results = results;
    }

    public List<MessageDTO> getResults() {
        return this.results;
    }

    public String toString() {
        return "{\"results\":\"" + this.results + "\"}";
    }
}

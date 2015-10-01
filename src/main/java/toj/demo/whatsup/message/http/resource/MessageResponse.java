package toj.demo.whatsup.message.http.resource;

import toj.demo.whatsup.message.dto.MessageDTO;
import toj.demo.whatsup.message.model.Message;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class MessageResponse {

    private List<MessageDTO> results;

    public MessageResponse(){

    }

    public MessageResponse(List<MessageDTO> results) {
        this.results = results;
    }

    public List<MessageDTO> getResults() {
        return results;
    }

    public void setResults(List<MessageDTO> results) {
        this.results = results;
    }
    @Override
    public String toString() {
        return "{\"results\":\"" + results + "\"}";
    }
}

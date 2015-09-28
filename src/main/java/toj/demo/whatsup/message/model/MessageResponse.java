package toj.demo.whatsup.message.model;

import toj.demo.whatsup.message.model.Message;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class MessageResponse {

    private List<Message> results;

    public MessageResponse(){

    }

    public MessageResponse(List<Message> results) {
        this.results = results;
    }

    public List<Message> getResults() {
        return results;
    }

    public void setResults(List<Message> results) {
        this.results = results;
    }
    @Override
    public String toString() {
        return "{\"results\":\"" + results + "\"}";
    }
}

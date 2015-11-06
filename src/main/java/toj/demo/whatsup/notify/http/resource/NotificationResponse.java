package toj.demo.whatsup.notify.http.resource;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
@XmlRootElement
public class NotificationResponse {
    private String sessionId;

    private String username;

    private Integer notifyPeriodInHours;

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                ", notifyPeriodInHours=" + notifyPeriodInHours +
                '}';
    }

    public NotificationResponse(){

    }
    public NotificationResponse(String sessionId, String username, Integer notifyPeriodInHours){
        this.sessionId = sessionId;
        this.username = username;
        this.notifyPeriodInHours=notifyPeriodInHours;
    }
    public int getNotifyPeriodInHours() {
        return notifyPeriodInHours;
    }
    public String getUsername() {
        return username;
    }
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationResponse that = (NotificationResponse) o;

        if (notifyPeriodInHours != null ? !notifyPeriodInHours.equals(that.notifyPeriodInHours) : that.notifyPeriodInHours != null)
            return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (notifyPeriodInHours != null ? notifyPeriodInHours.hashCode() : 0);
        return result;
    }

}

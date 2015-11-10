package toj.demo.whatsup.notify.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.AssignedStatus;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.notify.services.KeywordService;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@ContextConfiguration
public class NotifyResourceTest extends SpringManagedResourceTest<NotifyResource> {

    private  String sessionId;

    private  User user;

    private  Credentials credentialsMihai;

    @Autowired
    private UserService userService;
    @Autowired
    private UserSessionService userSessionService;
    @Resource(name="mailScheduler")
    private StdScheduler mailScheduler;


    private  static int count=0;

    @Before
    public void initialize(){
        credentialsMihai = new Credentials("Mihai"+count,"password","misuvd92"+count+"@yahoo.com");
        userService.signup(credentialsMihai);
        user = userService.get("Mihai"+count).get();
        sessionId = userSessionService.createUserSession(user);
    }
    @After
    public void after(){
        count++;
    }
    @Test
    public void testaddKeywords(){
        Response response=target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        assertEquals(response.readEntity(KeywordsSet.class).getKeywords(), new LinkedHashSet<>(Arrays.asList("Ioana","Maria","Mihaela")));
        assertEquals(user.getKeywords().size(),3);
    }
    @Test
    public void testChangePeriodSucceeds(){
        Response response=target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        NotificationResponse response1=response.readEntity(NotificationResponse.class);
        assertTrue(response1.equals(new NotificationResponse(sessionId,user.getUsername(),3)));
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",7).request().put(Entity.text(""));
        TriggerKey triggerKey=new TriggerKey("trigger-"+user.getId(),"mailTriggerGroup");
        Trigger trigger;
        try {
            trigger=mailScheduler.getTrigger(triggerKey);
            Date nextFireTime=trigger.getFireTimeAfter(trigger.getNextFireTime());
            assertEquals(nextFireTime, Date.from(Instant.ofEpochMilli(trigger.getNextFireTime().getTime()).plus(user.getNotificationPeriod(), ChronoUnit.HOURS)));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        target("notify/removejob").queryParam("sessionId",sessionId).request().delete();
    }
    @Test
    public void testChangePeriodAndAddKeywordsSucceeds()
    {
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana3\",\"Maria3\",\"Mihaela3\"]}"));
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela1\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        Response response=target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria2\",\"Mihaela2\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",7).request().put(Entity.text(""));
        assertEquals(user.getNotificationPeriod(),new Integer(7));
        Set<String> responseKeywords=response.readEntity(KeywordsSet.class).getKeywords();
        Set<String> expectedKeywords=new LinkedHashSet<String>(Arrays.asList("Maria2","Mihaela2"));
        expectedKeywords=expectedKeywords.stream().sorted().collect(Collectors.toSet());
        assertEquals(responseKeywords,expectedKeywords);
        assertEquals(user.getKeywords().size(),9);

    }

    @Test
    public void tesRemoveKeywordsSucceeds()
    {
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/removekeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\"]}"));
        Response response=target("notify/removekeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Maria\"]}"));
        User user=userService.get("Mihai"+count).get();
        assertEquals(user.getKeywords().size(), 1);
        assertEquals(response.readEntity(KeywordsSet.class).getKeywords(),new LinkedHashSet<String>(Arrays.asList("Maria")));
    }

    @Test
    public void testRequestNotifyJobSucceeds(){
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        Response response=target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        JobKey jobKey=new JobKey("job-"+user.getId(),"mailGroup");
        try {
            assertTrue(mailScheduler.checkExists(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user=userService.get("Mihai"+count).get();
        assertEquals(response.getStatusInfo(),Response.Status.OK);
        target("notify/removejob").queryParam("sessionId",sessionId).request().delete();
    }
    @Test
    public void testRemoveNotifyJobSucceeds(){
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        Response response=target("notify/removejob").queryParam("sessionId",sessionId).request().delete();
        JobKey jobKey=new JobKey("job-"+user.getId(),"mailGroup");
        try {
            assertFalse(mailScheduler.checkExists(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        TriggerKey triggerKey=new TriggerKey("trigger-"+user.getId(),"mailTriggerGroup");
        try {
            assertFalse(mailScheduler.checkExists(triggerKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        user=userService.get("Mihai"+count).get();
        assertEquals(response.getStatusInfo(),Response.Status.OK);
    }
    @Test
    public void testRemoveInexistentNotifyJobFails(){
        Response response=target("notify/removejob").queryParam("sessionId",sessionId).request().delete();
        assertEquals(response.getStatusInfo(),Response.Status.BAD_REQUEST);
    }
    @Test
    public void testRequestNotifyJobFailsForUserWithNoKeywordsOrNotifyPeriod(){
        Response response=target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        assertEquals(response.getStatusInfo(),Response.Status.FORBIDDEN);
    }
    @Test
    public void testRequestTwoNotifyJobs(){
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        Response response=target("notify/requestjob").queryParam("sessionId",sessionId).request().post(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }


}

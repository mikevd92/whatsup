package toj.demo.whatsup.mappers;

import org.quartz.SchedulerException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by mihai.popovici on 11/9/2015.
 */
public class SchedulerExceptionMapper implements ExceptionMapper<SchedulerException> {
    @Override
    public Response toResponse(SchedulerException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}

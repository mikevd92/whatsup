package toj.demo.whatsup.mappers;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by mihai.popovici on 10/15/2015.
 */
@Provider
public class NullPointerMapper implements ExceptionMapper<NullPointerException> {

    @Override
    public Response toResponse(NullPointerException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

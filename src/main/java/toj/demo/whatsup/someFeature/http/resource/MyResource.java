package toj.demo.whatsup.someFeature.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.someFeature.aComponent.Dependency;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("myresource")
public final class MyResource {

    private final Dependency dependency;

    @Autowired
    public MyResource(final Dependency dependency) {
        this.dependency = dependency;
    }

    @GET
    @Produces("text/plain")
    public String getIt() {
        return String.valueOf(dependency.isReal());
    }

}

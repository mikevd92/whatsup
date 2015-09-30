package toj.demo.whatsup.test.jersey;

import com.google.common.reflect.TypeToken;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.kubek2k.springockito.annotations.experimental.DirtiesMocksTestContextListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import toj.demo.whatsup.http.filter.AuthenticationFilter;
import toj.demo.whatsup.http.filter.SessionFilter;
import toj.demo.whatsup.message.http.resource.MessageResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

/**
 * Base class used for testing jersey resource that are managed by Spring.
 *
 * Adapted from: http://stackoverflow.com/a/24512682
 * Also uses: https://bitbucket.org/kubek2k/springockito/wiki/springockito-annotations
 *
 * @param <R> Type of the resource under test
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    loader = SpringockitoContextLoader.class,
    locations = {
        "classpath:jersey-spring-applicationContext.xml",
        "classpath:/toj/demo/whatsup/http/resource/resources.xml"
    }
)
@TestExecutionListeners({
    ServletTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    DirtiesMocksTestContextListener.class
})
public abstract class SpringManagedResourceTest<R> {

    private JerseyTest jerseyTest;

    public final WebTarget target(final String path) {
        return jerseyTest.target(path);
    }

    @Before
    public final void setUpTest() throws Exception {
        jerseyTest.setUp();
    }

    @After
    public final void tearDownTest() throws Exception {
        jerseyTest.tearDown();
    }

    @Autowired
    public final void setApplicationContext(final ApplicationContext context) {
        final TypeToken resource = new TypeToken<R>(getClass()) {};
        jerseyTest = new JerseyTest() {
            @Override
            protected Application configure() {
                return new ResourceConfig(
                    resource.getRawType()
                ).property("contextConfig", context).register(AuthenticationFilter.class).register(SessionFilter.class);
            }
        };

    }

}

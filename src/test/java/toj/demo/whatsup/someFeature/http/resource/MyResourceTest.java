package toj.demo.whatsup.someFeature.http.resource;

import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.someFeature.aComponent.Dependency;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ContextConfiguration
public class MyResourceTest extends SpringManagedResourceTest<MyResource> {

    @ReplaceWithMock
    @Autowired
    private Dependency dependency;

    @Test
    public void testMyResource() {
        final String expected = "false";
        when(dependency.isReal()).thenReturn(Boolean.parseBoolean(expected));
        final String actual = target("myresource").request().get(String.class);
        assertEquals(expected, actual);
    }

}

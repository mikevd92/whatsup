package toj.demo.whatsup.someFeature.aComponent.real;

import org.junit.Test;

import static org.junit.Assert.*;

public class RealDependencyTest {

    @Test
    public void testIsReal() throws Exception {
        assertTrue(new RealDependency().isReal());
    }

}

package toj.demo.whatsup.http.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mihai.popovici on 9/29/2015.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Session {
}

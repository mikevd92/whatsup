package toj.demo.whatsup.http.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mihai.popovici on 9/30/2015.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
}

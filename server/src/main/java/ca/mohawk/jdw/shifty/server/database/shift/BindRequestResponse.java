package ca.mohawk.jdw.shifty.server.database.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindRequestResponse.RequestResponseBinderFactory.class)
public @interface BindRequestResponse {

    String value() default "response_id";

    class RequestResponseBinderFactory implements BinderFactory {

        @Override
        public Binder<BindRequestResponse, RequestResponse> build(final Annotation annotation){
            return (q, bind, r) -> q.bind(bind.value(), r.id());
        }
    }
}

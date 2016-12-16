package ca.mohawk.jdw.shifty.server.database.activity;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.utils.Utils;
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
@BindingAnnotation(BindActivityLog.ActivityLogBinderFactory.class)
public @interface BindActivityLog {

    class ActivityLogBinderFactory implements BinderFactory {

        @Override
        public Binder<BindActivityLog, ActivityLog> build(final Annotation annotation){
            return (q, bind, log) -> {
                q.bind("employee_id", log.employee().id());
                q.bind("timestamp", Utils.timestampSqlString(log.timestamp()));
                q.bind("message", log.message());
            };
        }
    }
}

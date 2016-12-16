package ca.mohawk.jdw.shifty.server.database.activity;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.database.Dao;
import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import java.util.Collection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ActivityLogMapper.class)
public interface ActivityLogDao extends Dao {

    @SqlUpdate("INSERT INTO activity_logs " +
            "(employee_id, type_id, message) VALUES " +
            "(:employee_id, :type_id, :message)")
    int add(@Bind("employee_id") final long employeeId,
            @Bind("message") final String message);

    @SqlUpdate("INSERT INTO activity_logs " +
            "(employee_id, timestamp, message) VALUES " +
            "(:employee_id, :timestamp, :message)")
    int insert(@BindActivityLog final ActivityLog log);

    @SqlQuery("SELECT * FROM activity_logs WHERE " +
            "employee_id = :employee_id ORDER BY timestamp")
    Collection<ActivityLog> forEmployee(@Bind("employee_id") final long employeeId);
}

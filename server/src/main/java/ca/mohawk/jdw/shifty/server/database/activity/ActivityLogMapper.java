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
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


import static ca.mohawk.jdw.shifty.server.Shifty.company;

public class ActivityLogMapper implements ResultSetMapper<ActivityLog> {

    @Override
    public ActivityLog map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ActivityLog(
                company.employees().forId(rs.getLong("employee_id")),
                Utils.timestamp(rs.getString("timestamp")),
                rs.getString("message")
        );
    }
}

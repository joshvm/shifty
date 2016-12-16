package ca.mohawk.jdw.shifty.testcompany.db.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.util.Collection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ShiftMapper.class)
public interface SqliteShiftDao {

    @SqlQuery("SELECT * FROM shifts WHERE id = :id")
    Shift forId(@Bind("id") final long id);

    @SqlQuery("SELECT * FROM shifts WHERE employee_id = :employee_id")
    Collection<Shift> forEmployee(@Bind("employee_id") final long employeeId);

    @SqlQuery("SELECT * FROM shifts WHERE " +
            "start_timestamp >= :start_timestamp AND " +
            "finish_timestamp <= :finish_timestamp")
    Collection<Shift> between(@Bind("start_timestamp") final String startTimestamp,
                              @Bind("finish_timestamp") final String finishTimestamp);

    @SqlQuery("SELECT * FROM shifts WHERE " +
            "employee_id = :employee_id AND " +
            "start_timestamp >= :start_timestamp AND " +
            "finish_timestamp <= :finish_timestamp")
    Collection<Shift> between(@Bind("employee_id") final long employeeId,
                              @Bind("start_timestamp") final String startTimestamp,
                              @Bind("finish_timestamp") final String finishTimestamp);

    @SqlUpdate("UPDATE shifts SET employee_id = :employee_id " +
            "WHERE id = :id")
    int setEmployee(@Bind("id") final long id,
                    @Bind("employee_id") final long employeeId);
}

package ca.mohawk.jdw.shifty.server.database.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.database.Dao;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import java.util.Collection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ShiftOfferMapper.class)
public interface ShiftOfferDao extends Dao {

    @SqlQuery("SELECT * FROM shift_offers WHERE shift_id = :shift_id")
    ShiftOffer forId(@Bind("shift_id") final long shiftId);

    @SqlQuery("SELECT * FROM shift_offers WHERE employee_id = :employee_id")
    Collection<ShiftOffer> forEmployee(@Bind("employee_id") final long employeeId);

    @SqlUpdate("INSERT INTO shift_offers " +
            "(shift_id, employee_id) VALUES " +
            "(:shift_id, :employee_id)")
    int add(@Bind("shift_id") final long shiftId,
            @Bind("employee_id") final long employeeId);

    @SqlUpdate("DELETE FROM shift_offers WHERE shift_id = :shift_id")
    int remove(@Bind("shift_id") final long shiftId);
}

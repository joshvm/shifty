package ca.mohawk.jdw.shifty.companyapi.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import java.sql.Timestamp;
import java.util.Collection;

public interface ShiftDao {

    Shift forId(final long id);

    Collection<Shift> forEmployee(final Employee employee);
    
    Collection<Shift> between(final Timestamp startTime,
                              final Timestamp finishTimestamp);
    
    Collection<Shift> between(final Employee employee,
                              final Timestamp startTimestamp,
                              final Timestamp finishTimestamp);

    boolean setEmployee(final Shift shift,
                        final Employee employee);
}

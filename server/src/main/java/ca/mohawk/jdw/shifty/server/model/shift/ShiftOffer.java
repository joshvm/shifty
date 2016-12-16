package ca.mohawk.jdw.shifty.server.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.sql.Timestamp;

public class ShiftOffer {

    private final Timestamp timestamp;
    private final Shift shift;
    private final Employee employee;

    public ShiftOffer(final Timestamp timestamp,
                      final Shift shift,
                      final Employee employee){
        this.timestamp = timestamp;
        this.shift = shift;
        this.employee = employee;
    }

    public long id(){
        return shift.id();
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public Shift shift(){
        return shift;
    }

    public Employee employee(){
        return employee;
    }

}

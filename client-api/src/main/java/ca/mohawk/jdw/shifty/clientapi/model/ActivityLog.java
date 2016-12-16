package ca.mohawk.jdw.shifty.clientapi.model;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.Model;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import java.sql.Timestamp;

public class ActivityLog implements Model {

    private final Employee employee;
    private final Timestamp timestamp;
    private final String message;

    public ActivityLog(final Employee employee,
                       final Timestamp timestamp,
                       final String message){
        this.employee = employee;
        this.timestamp = timestamp;
        this.message = message;
    }

    @Override
    public long id(){
        return timestamp.getTime();
    }

    public Employee employee(){
        return employee;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public String message(){
        return message;
    }
}

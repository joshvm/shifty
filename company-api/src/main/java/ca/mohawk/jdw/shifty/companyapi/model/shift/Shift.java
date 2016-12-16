package ca.mohawk.jdw.shifty.companyapi.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.Model;
import java.sql.Timestamp;

public class Shift implements Model {

    private final long id;
    private final long employeeId;

    private final Timestamp startTimestamp;
    private final Timestamp finishTimestamp;

    private final String description;

    public Shift(final long id,
                 final long employeeId,
                 final Timestamp startTimestamp,
                 final Timestamp finishTimestamp,
                 final String description){
        this.id = id;
        this.employeeId = employeeId;
        this.startTimestamp = startTimestamp;
        this.finishTimestamp = finishTimestamp;
        this.description = description;
    }

    public long id(){
        return id;
    }

    public long employeeId(){
        return employeeId;
    }

    public Timestamp startTimestamp(){
        return startTimestamp;
    }

    public Timestamp finishTimestamp(){
        return finishTimestamp;
    }

    public String description(){
        return description;
    }
}

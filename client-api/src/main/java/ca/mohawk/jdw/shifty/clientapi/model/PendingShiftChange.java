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
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;

public class PendingShiftChange implements Model {

    private final Employee offeringEmployee;
    private final Shift shift;
    private final Employee requestingEmployee;

    public PendingShiftChange(final Employee offeringEmployee,
                              final Shift shift,
                              final Employee requestingEmployee){
        this.offeringEmployee = offeringEmployee;
        this.shift = shift;
        this.requestingEmployee = requestingEmployee;
    }

    @Override
    public long id(){
        return shift.id();
    }

    public Employee offeringEmployee(){
        return offeringEmployee;
    }

    public Shift shift(){
        return shift;
    }

    public Employee requestingEmployee(){
        return requestingEmployee;
    }
}

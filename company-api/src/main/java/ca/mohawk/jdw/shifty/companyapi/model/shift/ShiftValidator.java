package ca.mohawk.jdw.shifty.companyapi.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;

public interface ShiftValidator {

    boolean canOffer(final Employee offeringEmployee,
                     final Shift shift);

    boolean canRequest(final Employee offeringEmployee,
                       final Shift shift,
                       final Employee requestingEmployee);
}

package ca.mohawk.jdw.shifty.companyapi.model.employee;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

public interface EmployeeDao {

    Employee forId(final long id);

    Employee forUser(final String user);
}

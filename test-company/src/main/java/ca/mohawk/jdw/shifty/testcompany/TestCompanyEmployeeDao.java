package ca.mohawk.jdw.shifty.testcompany;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.employee.EmployeeDao;
import ca.mohawk.jdw.shifty.testcompany.db.TestCompanyDatabase;

public class TestCompanyEmployeeDao implements EmployeeDao {

    private final TestCompanyDatabase db;

    public TestCompanyEmployeeDao(final TestCompanyDatabase db){
        this.db = db;
    }

    @Override
    public Employee forId(final long id){
        return db.employees().forId(id);
    }

    @Override
    public Employee forUser(final String user){
        return db.employees().forUser(user);
    }
}

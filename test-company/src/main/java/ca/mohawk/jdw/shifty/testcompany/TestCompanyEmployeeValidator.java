package ca.mohawk.jdw.shifty.testcompany;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.employee.EmployeeValidator;
import ca.mohawk.jdw.shifty.testcompany.db.TestCompanyDatabase;

public class TestCompanyEmployeeValidator implements EmployeeValidator {

    private final TestCompanyDatabase db;

    public TestCompanyEmployeeValidator(final TestCompanyDatabase db){
        this.db = db;
    }

    @Override
    public boolean isPassword(final Employee employee, final String pass){
        return pass.equals(db.employees().pass(employee.id()));
    }
}

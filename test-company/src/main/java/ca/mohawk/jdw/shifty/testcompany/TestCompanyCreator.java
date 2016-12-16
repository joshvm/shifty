package ca.mohawk.jdw.shifty.testcompany;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.Address;
import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.companyapi.creator.CompanyCreator;
import ca.mohawk.jdw.shifty.testcompany.db.TestCompanyDatabase;

public class TestCompanyCreator implements CompanyCreator {

    @Override
    public Company create(){
        final TestCompanyDatabase db = new TestCompanyDatabase();
        return new Company.Builder()
                .name("Test Company")
                .email("admin@testcompany.com")
                .phone("905-123-4567")
                .address(
                        new Address.Builder()
                            .unit(1)
                            .street("Test St W")
                            .city("Hamilton, ON")
                            .country("Canada")
                            .build()
                )
                .employees(new TestCompanyEmployeeDao(db))
                .employeeValidator(new TestCompanyEmployeeValidator(db))
                .shifts(new TestCompanyShiftDao(db))
                .shiftValidator(new TestCompanyShiftValidator())
                .build();
    }
}

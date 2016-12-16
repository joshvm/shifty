package ca.mohawk.jdw.shifty.testcompany;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/


import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.companyapi.model.shift.ShiftDao;
import ca.mohawk.jdw.shifty.testcompany.db.TestCompanyDatabase;
import java.sql.Timestamp;
import java.util.Collection;

public class TestCompanyShiftDao implements ShiftDao {

    private final TestCompanyDatabase db;

    public TestCompanyShiftDao(final TestCompanyDatabase db){
        this.db = db;
    }

    @Override
    public Shift forId(final long id){
        return db.shifts().forId(id);
    }

    @Override
    public Collection<Shift> forEmployee(final Employee employee){
        return db.shifts().forEmployee(employee.id());
    }

    @Override
    public Collection<Shift> between(final Timestamp startTimestamp,
                                     final Timestamp finishTimestamp){
        return db.shifts().between(
                TestCompanyDatabase.TIMESTAMP_FORMAT.format(startTimestamp),
                TestCompanyDatabase.TIMESTAMP_FORMAT.format(finishTimestamp)
        );
    }

    @Override
    public Collection<Shift> between(final Employee employee,
                                     final Timestamp startTimestamp,
                                     final Timestamp finishTimestamp){
        return db.shifts().between(
                employee.id(),
                TestCompanyDatabase.TIMESTAMP_FORMAT.format(startTimestamp),
                TestCompanyDatabase.TIMESTAMP_FORMAT.format(finishTimestamp)
        );
    }

    @Override
    public boolean setEmployee(final Shift shift, final Employee employee){
        return db.shifts().setEmployee(shift.id(), employee.id()) > 0;
    }
}

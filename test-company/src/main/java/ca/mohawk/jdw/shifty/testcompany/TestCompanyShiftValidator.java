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
import ca.mohawk.jdw.shifty.companyapi.model.shift.ShiftValidator;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class TestCompanyShiftValidator implements ShiftValidator {

    private static final List<LocalDate> HOLIDAYS = Arrays.asList(
            LocalDate.of(0, Month.DECEMBER, 24),
            LocalDate.of(0, Month.DECEMBER, 25)
    );

    @Override
    public boolean canOffer(final Employee offeringEmployee,
                            final Shift shift){
        final LocalDate date = shift.startTimestamp()
                .toLocalDateTime()
                .toLocalDate()
                .withYear(0);
        return !HOLIDAYS.contains(date);
    }

    @Override
    public boolean canRequest(final Employee offeringEmployee,
                              final Shift shift,
                              final Employee requestingEmployee){
        return offeringEmployee.rank() == requestingEmployee.rank();
    }
}

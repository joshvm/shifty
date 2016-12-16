package ca.mohawk.jdw.shifty.testcompany.db.employee;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(EmployeeMapper.class)
public interface SqliteEmployeeDao {

    @SqlQuery("SELECT * FROM employees WHERE id = :id")
    Employee forId(@Bind("id") final long id);

    @SqlQuery("SELECT * FROM employees WHERE user = :user")
    Employee forUser(@Bind("user") final String user);

    @SqlQuery("SELECT pass FROM employees WHERE id = :id")
    String pass(@Bind("id") final long id);
}

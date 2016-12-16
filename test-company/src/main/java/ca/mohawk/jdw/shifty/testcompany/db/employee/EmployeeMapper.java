package ca.mohawk.jdw.shifty.testcompany.db.employee;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class EmployeeMapper implements ResultSetMapper<Employee> {

    @Override
    public Employee map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Employee(
                rs.getLong("id"),
                rs.getString("user"),
                Employee.Rank.forId(rs.getInt("rank_id")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("description")
        );
    }
}

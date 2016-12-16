package ca.mohawk.jdw.shifty.testcompany.db.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ShiftMapper implements ResultSetMapper<Shift> {

    public static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Shift map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        try{
            return new Shift(
                    rs.getLong("id"),
                    rs.getLong("employee_id"),
                    new Timestamp(FORMAT.parse(rs.getString("start_timestamp")).getTime()),
                    new Timestamp(FORMAT.parse(rs.getString("finish_timestamp")).getTime()),
                    rs.getString("description")
            );
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}

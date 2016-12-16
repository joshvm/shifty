package ca.mohawk.jdw.shifty.server.database.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


import static ca.mohawk.jdw.shifty.server.Shifty.company;

public class ShiftOfferMapper implements ResultSetMapper<ShiftOffer> {

    @Override
    public ShiftOffer map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ShiftOffer(
                Utils.timestamp(rs.getString("timestamp")),
                company.shifts().forId(rs.getLong("shift_id")),
                company.employees().forId(rs.getLong("employee_id"))
        );
    }
}

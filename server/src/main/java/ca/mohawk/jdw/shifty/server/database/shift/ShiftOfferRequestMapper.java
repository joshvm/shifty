package ca.mohawk.jdw.shifty.server.database.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class ShiftOfferRequestMapper implements ResultSetMapper<ShiftOfferRequest> {

    @Override
    public ShiftOfferRequest map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException {
        final int requestResponseId = rs.getInt("response_id");
        final RequestResponse response = rs.wasNull() ? null
                : RequestResponse.forId(requestResponseId);
        return new ShiftOfferRequest(
                Utils.timestamp(rs.getString("timestamp")),
                database.shiftOffers().forId(rs.getLong("offer_id")),
                company.employees().forId(rs.getLong("employee_id")),
                response
        );
    }
}

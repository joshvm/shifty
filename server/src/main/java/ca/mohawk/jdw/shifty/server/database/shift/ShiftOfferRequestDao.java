package ca.mohawk.jdw.shifty.server.database.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.database.Dao;
import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import java.util.Collection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ShiftOfferRequestMapper.class)
public interface ShiftOfferRequestDao extends Dao {

//    @SqlUpdate("CREATE TABLE IF NOT EXISTS shift_offer_requests (" +
//            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
//            "offer_id BIGINT NOT NULL, " +
//            "employee_id BIGINT NOT NULL, " +
//            "response_id INT NOT NULL DEFAULT 0)")
//    void init();

    @SqlQuery("SELECT * FROM shift_offer_requests WHERE " +
            "offer_id = :offer_id AND " +
            "employee_id = :employee_id")
    ShiftOfferRequest get(@Bind("offer_id") final long offerId,
                          @Bind("employee_id") final long employeeId);

    @SqlQuery("SELECT * FROM shift_offer_requests WHERE " +
            "offer_id = :offer_id")
    Collection<ShiftOfferRequest> forOffer(@Bind("offer_id") final long offerId);

    @SqlQuery("SELECT * FROM shift_offer_requests WHERE " +
            "employee_id = :employee_id")
    Collection<ShiftOfferRequest> forEmployee(@Bind("employee_id") final long employeeId);

    @SqlQuery("SELECT * FROM shift_offer_requests WHERE " +
            "offer_id = :offer_id AND " +
            "response_id = :response_id")
    Collection<ShiftOfferRequest> forOfferByResponse(@Bind("offer_id") final long offerId,
                                                     @BindRequestResponse("response_id") final RequestResponse response);

    @SqlQuery("SELECT * FROM shift_offer_requests WHERE " +
            "response_id = :response_id")
    Collection<ShiftOfferRequest> forResponse(@BindRequestResponse("response_id") final RequestResponse response);

    @SqlUpdate("INSERT INTO shift_offer_requests " +
            "(offer_id, employee_id, response_id) VALUES " +
            "(:offer_id, :employee_id, :response_id)")
    int add(@Bind("offer_id") final long offerId,
            @Bind("employee_id") final long employeeId,
            @BindRequestResponse("response_id") final RequestResponse response);

    @SqlUpdate("DELETE FROM shift_offer_requests WHERE offer_id = :offer_id")
    int remove(@Bind("offer_id") final long offerId);

    @SqlUpdate("DELETE FROM shift_offer_requests WHERE offer_id = :offer_id " +
            "AND employee_id = :employee_id")
    int remove(@Bind("offer_id") final long offerId,
               @Bind("employee_id") final long employeeId);

    @SqlUpdate("UPDATE shift_offer_requests SET response_id = :response_id " +
            "WHERE offer_id = :offer_id AND employee_id = :employee_id")
    int updateResponse(@Bind("offer_id") final long offerId,
                       @Bind("employee_id") final long employeeId,
                       @BindRequestResponse("response_id") final RequestResponse response);
}

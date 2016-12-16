package ca.mohawk.jdw.shifty.server.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import java.sql.Timestamp;

public class ShiftOfferRequest {

    private final Timestamp timestamp;
    private final ShiftOffer offer;
    private final Employee employee;
    private final RequestResponse response;

    public ShiftOfferRequest(final Timestamp timestamp,
                             final ShiftOffer offer,
                             final Employee employee,
                             final RequestResponse response){
        this.timestamp = timestamp;
        this.offer = offer;
        this.employee = employee;
        this.response = response;
    }

    public long id(){
        return offer.id();
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public ShiftOffer offer(){
        return offer;
    }

    public Employee employee(){
        return employee;
    }

    public RequestResponse response(){
        return response;
    }
}

package ca.mohawk.jdw.shifty.server.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.util.Map;


import static ca.mohawk.jdw.shifty.server.Shifty.database;

public enum ShiftState {

    NONE(0),
    OFFERED(1),
    PENDING(2);

    private static final Map<Integer, ShiftState> MAP = Utils.map(values(), ShiftState::id);

    private final int id;

    ShiftState(final int id){
        this.id = id;
    }

    public int id(){
        return id;
    }

    public static ShiftState forId(final int id){
        return MAP.get(id);
    }

    public static ShiftState state(final Shift shift){
        final ShiftOffer offer = database.shiftOffers().forId(shift.id());
        if(offer == null)
            return NONE;
        if(!database.shiftOfferRequests().forOfferByResponse(shift.id(), RequestResponse.ACCEPTED).isEmpty())
            return PENDING;
        return OFFERED;
    }
}

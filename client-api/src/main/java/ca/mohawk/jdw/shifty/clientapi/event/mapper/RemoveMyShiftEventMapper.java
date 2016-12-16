package ca.mohawk.jdw.shifty.clientapi.event.mapper;

/*
  I, Josh Maione, 000320309 certify that this material is my original work.
  No other person's work has been used without due acknowledgement.
  I have not made my work available to anyone else.

  Module: client-api

  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.EventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveMyShiftEvent;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;

public class RemoveMyShiftEventMapper implements EventMapper<RemoveMyShiftEvent> {

    @Override
    public RemoveMyShiftEvent map(final ShiftyClient client, final Packet pkt){
        final long shiftId = pkt.readLong();
        final Shift shift = client.myShifts().forId(shiftId);
        if(shift == null)
            return null;
        return new RemoveMyShiftEvent(shift);
    }
}

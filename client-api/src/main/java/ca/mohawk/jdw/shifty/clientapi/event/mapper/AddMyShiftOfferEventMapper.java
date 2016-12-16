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
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class AddMyShiftOfferEventMapper implements EventMapper<AddMyShiftOfferEvent> {

    @Override
    public AddMyShiftOfferEvent map(final ShiftyClient client, final Packet pkt){
        return new AddMyShiftOfferEvent(
                new ShiftOffer(
                        client.employee(),
                        client.myShifts().forId(pkt.readLong())
                )
        );
    }
}

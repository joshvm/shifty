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
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveMyShiftOfferRequestEvent;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class RemoveMyShiftOfferRequestEventMapper implements EventMapper<RemoveMyShiftOfferRequestEvent> {

    @Override
    public RemoveMyShiftOfferRequestEvent map(final ShiftyClient client, final Packet pkt){
        final ShiftOffer.Request request = client.myShiftOfferRequests().forId(pkt.readLong());
        if(request == null)
            return null;
        return new RemoveMyShiftOfferRequestEvent(
                request
        );
    }
}

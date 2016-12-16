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
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveRequestFromMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class RemoveRequestFromMyShiftOfferEventMapper implements EventMapper<RemoveRequestFromMyShiftOfferEvent> {

    @Override
    public RemoveRequestFromMyShiftOfferEvent map(final ShiftyClient client, final Packet pkt){
        final long offerId = pkt.readLong();
        final long employeeId = pkt.readLong();
        final ShiftOffer offer = client.myShiftOffers().forId(offerId);
        if(offer == null)
            return null;
        final ShiftOffer.Request request = offer.requests().list()
                .stream()
                .filter(r -> r.employee().id() == employeeId)
                .findFirst()
                .orElse(null);
        if(request == null)
            return null;
        return new RemoveRequestFromMyShiftOfferEvent(
                request
        );
    }
}

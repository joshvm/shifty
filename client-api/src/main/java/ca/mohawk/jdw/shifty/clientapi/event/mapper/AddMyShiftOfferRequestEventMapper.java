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
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyShiftOfferRequestEvent;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class AddMyShiftOfferRequestEventMapper implements EventMapper<AddMyShiftOfferRequestEvent> {

    @Override
    public AddMyShiftOfferRequestEvent map(final ShiftyClient client, final Packet pkt){
        final ShiftOffer offer = client.availableShifts().forId(pkt.readLong());
        final RequestAnswer answer = RequestAnswer.forId(pkt.readUnsignedByte());
        if(offer == null || answer == null)
            return null;
        return new AddMyShiftOfferRequestEvent(
                new ShiftOffer.Request(offer, client.employee(), answer)
        );
    }
}

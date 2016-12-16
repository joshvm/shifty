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
import ca.mohawk.jdw.shifty.clientapi.event.type.UpdateMyShiftOfferRequestResponseEvent;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class UpdateMyShiftOfferRequestResponseEventMapper implements EventMapper<UpdateMyShiftOfferRequestResponseEvent> {

    @Override
    public UpdateMyShiftOfferRequestResponseEvent map(final ShiftyClient client, final Packet pkt){
        final long requestId = pkt.readLong();
        final RequestAnswer answer = RequestAnswer.forId(pkt.readUnsignedByte());
        final ShiftOffer.Request request = client.myShiftOfferRequests().forId(requestId);
        if(request == null)
            return null;
        return new UpdateMyShiftOfferRequestResponseEvent(
                request,
                answer
        );
    }
}

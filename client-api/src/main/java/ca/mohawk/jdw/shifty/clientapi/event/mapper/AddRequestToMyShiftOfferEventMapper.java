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
import ca.mohawk.jdw.shifty.clientapi.event.type.AddRequestToMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.clientapi.utils.NetUtils;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;

public class AddRequestToMyShiftOfferEventMapper implements EventMapper<AddRequestToMyShiftOfferEvent> {

    @Override
    public AddRequestToMyShiftOfferEvent map(final ShiftyClient client, final Packet pkt){
        final ShiftOffer offer = client.myShiftOffers().forId(pkt.readLong());
        final Employee employee = NetUtils.deserializeEmployee(pkt);
        final RequestAnswer answer = RequestAnswer.forId(pkt.readUnsignedByte());
        if(offer == null || answer == null)
            return null;
        return new AddRequestToMyShiftOfferEvent(
                new ShiftOffer.Request(offer, employee, answer)
        );
    }
}

package ca.mohawk.jdw.shifty.server.net.event.impl;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.ClientPacketListener;
import java.util.Objects;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class RequestToTakeShiftListener implements ClientPacketListener {

    private static final int OFFER_NOT_FOUND = 1;
    private static final int CANT_REQUEST_OWN_OFFER = 2;
    private static final int ALREADY_REQUESTING = 3;
    private static final int CANNOT_REQUEST = 4;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long offerId = pkt.readLong();
        final ShiftOffer offer = database.shiftOffers().forId(offerId);
        if(offer == null){
            client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, OFFER_NOT_FOUND);
            return;
        }
        if(Objects.equals(offer.employee(), client.employee())){
            client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, CANT_REQUEST_OWN_OFFER);
            return;
        }
        if(!company.shiftValidator().canRequest(offer.employee(), offer.shift(), client.employee())){
            client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, CANNOT_REQUEST);
            return;
        }
        if(database.shiftOfferRequests().get(offer.id(), client.employee().id()) != null){
            client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, ALREADY_REQUESTING);
            return;
        }
        database.shiftOfferRequests().add(offer.id(), client.employee().id(), RequestResponse.PENDING);
        final ShiftOfferRequest request = database.shiftOfferRequests().get(offer.id(), client.employee().id());
        client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, SUCCESS);
        final ActivityLog requestingLog = ActivityLog.requestToTakeShift(request);
        database.activityLogs().insert(requestingLog);
        client.addMyShiftOfferRequest(request);
        client.addMyActivityLog(requestingLog);
        client.flush();
        final ActivityLog offeringLog = ActivityLog.requestAddedToMyShiftOffer(request);
        database.activityLogs().insert(offeringLog);
        final Client offeringClient = clients.forId(offer.employee().id());
        if(offeringClient != null){
            offeringClient.addRequestToMyShiftOffer(request);
            offeringClient.addMyActivityLog(offeringLog);
            offeringClient.flush();
        }
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE, ERROR);
    }
}

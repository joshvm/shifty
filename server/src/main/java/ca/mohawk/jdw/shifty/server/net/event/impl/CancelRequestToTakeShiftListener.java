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
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.ClientPacketListener;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class CancelRequestToTakeShiftListener implements ClientPacketListener {

    private static final int REQUEST_NOT_FOUND = 1;
    private static final int ALREADY_ACCEPTED = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long offerId = pkt.readLong();
        final ShiftOfferRequest request = database.shiftOfferRequests().get(offerId, client.employee().id());
        if(request == null){
            client.writeResponse(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, REQUEST_NOT_FOUND);
            return;
        }
        if(request.response() == RequestResponse.ACCEPTED){
            client.writeResponse(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, ALREADY_ACCEPTED);
            return;
        }
        database.shiftOfferRequests().remove(offerId, client.employee().id());
        client.writeResponse(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, SUCCESS);
        final ActivityLog requesterLog = ActivityLog.cancelRequestToTakeShift(request);
        database.activityLogs().insert(requesterLog);
        client.removeMyShiftOfferRequest(request);
        client.addMyActivityLog(requesterLog);
        client.flush();
        final ActivityLog offeringLog = ActivityLog.requestCancelledFromMyShiftOffer(request);
        database.activityLogs().insert(offeringLog);
        final Client offeringClient = clients.forId(request.offer().employee().id());
        if(offeringClient != null){
            offeringClient.removeRequestFromMyShiftOffer(request);
            offeringClient.addMyActivityLog(offeringLog);
            offeringClient.flush();
        }
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, ERROR);
    }
}

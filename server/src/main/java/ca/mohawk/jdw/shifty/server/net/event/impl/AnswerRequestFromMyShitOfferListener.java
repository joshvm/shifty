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
import java.util.Collection;
import java.util.Objects;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class AnswerRequestFromMyShitOfferListener implements ClientPacketListener {

    private static final int INVALID_RESPONSE = 1;
    private static final int REQUEST_NOT_FOUND = 2;
    private static final int NOT_MY_OFFER = 3;
    private static final int ALREADY_ANSWERED = 4;
    private static final int ALREADY_ACCEPTED_A_REQUEST = 5;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long offerId = pkt.readLong();
        final long employeeId = pkt.readLong();
        final RequestResponse response = RequestResponse.forId(pkt.readUnsignedByte());
        if(response == null){
            client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, INVALID_RESPONSE);
            return;
        }
        final ShiftOfferRequest request = database.shiftOfferRequests().get(offerId, employeeId);
        if(request == null){
            client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, REQUEST_NOT_FOUND);
            return;
        }
        if(!Objects.equals(request.offer().employee(), client.employee())){
            client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, NOT_MY_OFFER);
            return;
        }
        if(request.response() != RequestResponse.PENDING){
            client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, ALREADY_ANSWERED);
            return;
        }
        final Collection<ShiftOfferRequest> acceptedRequests = database.shiftOfferRequests()
                .forOfferByResponse(offerId, RequestResponse.ACCEPTED);
        if(!acceptedRequests.isEmpty()){
            client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, ALREADY_ACCEPTED_A_REQUEST);
            return;
        }
        database.shiftOfferRequests().updateResponse(offerId, employeeId, response);
        client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, SUCCESS);
        final ActivityLog offeringLog = ActivityLog.answerRequestFromMyShiftOffer(request, response);
        database.activityLogs().insert(offeringLog);
        client.addMyActivityLog(offeringLog);
        client.updateRequestResponseFromMyShiftOffer(request, response);
        client.flush();
        final ActivityLog requestingLog = ActivityLog.updateMyShiftOfferRequestResponse(request, response);
        database.activityLogs().insert(requestingLog);
        final Client requestingClient = clients.forId(employeeId);
        if(requestingClient != null){
            requestingClient.updateMyShiftOfferRequestResponse(request, response);
            requestingClient.addMyActivityLog(requestingLog);
            requestingClient.flush();
        }
        if(response == RequestResponse.ACCEPTED){
            clients.stream()
                    .forEach(c -> {
                        if(c.employee().isManager()){
                            c.addPendingShiftChange(request);
                            c.flush();
                        }
                    });
        }
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, ERROR);
    }
}

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
import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class AnswerPendingShiftChangeListener implements ClientPacketListener {

    private static final int NOT_FOUND = 1;
    private static final int INVALID_ANSWER = 2;
    private static final int NOT_A_MANAGER = 3;
    private static final int UNABLE_TO_FINALIZE = 4;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long offerId = pkt.readLong();
        final long employeeId = pkt.readLong();
        final RequestResponse response = RequestResponse.forId(pkt.readUnsignedByte());
        if(!client.employee().isManager()){
            client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, NOT_A_MANAGER);
            return;
        }
        final ShiftOfferRequest request = database.shiftOfferRequests().get(offerId, employeeId);
        if(request == null || request.response() != RequestResponse.ACCEPTED){
            client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, NOT_FOUND);
            return;
        }
        if(response == null){
            client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, INVALID_ANSWER);
            return;
        }
        client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, SUCCESS);
        final ActivityLog managerLog = ActivityLog.answeredPendingShiftChange(client.employee(), request, response);
        final ActivityLog offeringLog = ActivityLog.pendingShiftChangeAnsweredForOfferingEmployee(client.employee(), request, response);
        final ActivityLog requestingLog = ActivityLog.pendingShiftChangeAnsweredForRequestingEmployee(client.employee(), request, response);
        final Client offeringClient = clients.forId(request.offer().employee().id());
        final Client requestingClient = clients.forId(request.employee().id());
        if(response == RequestResponse.ACCEPTED){
            if(!company.shifts().setEmployee(request.offer().shift(), request.employee())){
                client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, UNABLE_TO_FINALIZE);
                return;
            }
            database.shiftOffers().remove(offerId);
            clients.stream()
                    .filter(c -> c.employee().isManager())
                    .forEach(c -> {
                        c.removePendingShiftChange(request);
                        c.flush();
                    });
            database.shiftOfferRequests().forOffer(offerId)
                    .stream()
                    .filter(r -> r.employee().id() != request.employee().id())
                    .forEach(r -> {
                        final ActivityLog log = ActivityLog.shiftOfferNoLongerAvailable(request);
                        database.activityLogs().insert(log);
                        final Client c = clients.forId(r.employee().id());
                        if(c != null){
                            c.removeMyShiftOfferRequest(r);
                            c.addMyActivityLog(log);
                            c.flush();
                        }
                    });
            database.shiftOfferRequests().remove(offerId);
            clients.stream()
                    .filter(c -> company.shiftValidator().canRequest(request.offer().employee(), request.offer().shift(), c.employee()))
                    .forEach(c -> {
                        c.removeAvailableShift(request.offer());
                        c.flush();
                    });
        }else if(response == RequestResponse.DENIED){
            database.shiftOfferRequests().remove(offerId, employeeId);
        }
        database.activityLogs().insert(managerLog);
        client.addMyActivityLog(managerLog);
        client.flush();
        database.activityLogs().insert(offeringLog);
        if(offeringClient != null){
            offeringClient.removeMyShiftOffer(request.offer());
            offeringClient.removeRequestFromMyShiftOffer(request);
            offeringClient.addMyActivityLog(offeringLog);
            offeringClient.removeMyShift(request.offer().shift());
            offeringClient.flush();
        }
        database.activityLogs().insert(requestingLog);
        if(requestingClient != null){
            requestingClient.addMyActivityLog(requestingLog);
            requestingClient.removeMyShiftOfferRequest(request);
            requestingClient.addMyShift(request.offer().shift());
            requestingClient.flush();
        }
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, ERROR);
    }
}

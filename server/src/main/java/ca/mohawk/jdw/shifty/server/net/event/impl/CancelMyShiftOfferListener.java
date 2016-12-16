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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class CancelMyShiftOfferListener implements ClientPacketListener {

    private static final int SHIFT_NOT_FOUND = 1;
    private static final int NOT_MY_OFFER = 2;
    private static final int ALREADY_ACCEPTED = 3;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long offerId = pkt.readLong();
        final ShiftOffer offer = database.shiftOffers().forId(offerId);
        if(offer == null){
            client.writeResponse(Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE, SHIFT_NOT_FOUND);
            return;
        }
        if(!Objects.equals(client.employee(), offer.employee())){
            client.writeResponse(Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE, NOT_MY_OFFER);
            return;
        }
        if(!database.shiftOfferRequests().forOfferByResponse(offerId, RequestResponse.ACCEPTED).isEmpty()){
            client.writeResponse(Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE, ALREADY_ACCEPTED);
            return;
        }
        client.writeResponse(Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE, SUCCESS);
        final ActivityLog log = ActivityLog.cancelMyShiftOffer(offer);
        database.activityLogs().insert(log);
        client.removeMyShiftOffer(offer);
        client.addMyActivityLog(log);
        client.flush();
        final Map<Long, ShiftOfferRequest> requestMap = database.shiftOfferRequests()
                .forOffer(offer.id())
                .stream()
                .collect(Collectors.toMap(r -> r.employee().id(), r -> r));
        final Map<Long, ActivityLog> requesterLogMap = requestMap.values()
                .stream()
                .collect(Collectors.toMap(r -> r.employee().id(), ActivityLog::shiftOfferCancelled));
        requesterLogMap.values()
                .forEach(database.activityLogs()::insert);
        clients.stream()
                .filter(c -> !c.equals(client))
                .forEach(c -> {
                    c.removeAvailableShift(offer);
                    final ShiftOfferRequest request = requestMap.get(c.employee().id());
                    if(request != null){
                        c.removeMyShiftOfferRequest(request);
                        c.addMyActivityLog(requesterLogMap.get(c.employee().id()));
                    }
                    c.flush();
                });
        database.shiftOffers().remove(offer.id());
        database.shiftOfferRequests().remove(offer.id());
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE, ERROR);
    }
}

package ca.mohawk.jdw.shifty.server.net.event.impl;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.ClientPacketListener;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.util.Objects;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class OfferMyShiftListener implements ClientPacketListener {

    private static final int SHIFT_NOT_FOUND = 1;
    private static final int NOT_MY_SHIFT = 2;
    private static final int TOO_LATE_TO_OFFER = 3;
    private static final int ALREADY_OFFERING = 4;
    private static final int CANNOT_OFFER = 5;

    private static final int SUCCESS = 100;
    private static final int FAIL = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final long shiftId = pkt.readLong();
        final Shift shift = company.shifts().forId(shiftId);
        if(shift == null){
            client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, SHIFT_NOT_FOUND);
            return;
        }
        if(shift.employeeId() != client.employee().id()){
            client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, NOT_MY_SHIFT);
            return;
        }
        if(Utils.timestamp().after(shift.startTimestamp())){
            client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, TOO_LATE_TO_OFFER);
            return;
        }
        if(database.shiftOffers().forId(shift.id()) != null){
            client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, ALREADY_OFFERING);
            return;
        }
        if(!company.shiftValidator().canOffer(client.employee(), shift)){
            client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, CANNOT_OFFER);
            return;
        }
        database.shiftOffers().add(shift.id(), shift.employeeId());
        client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, SUCCESS);
        final ShiftOffer offer = database.shiftOffers().forId(shift.id());
        final ActivityLog log = ActivityLog.offerMyShift(offer);
        database.activityLogs().insert(log);
        client.addMyShiftOffer(offer);
        client.addMyActivityLog(log);
        client.flush();
        //send the shift offer to other employees
        clients.stream()
                .filter(c -> !Objects.equals(c, client))
                .forEach(c -> {
                    c.addAvailableShift(offer);
                    c.flush();
                });
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.OFFER_MY_SHIFT_RESPONSE, FAIL);
    }
}

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
import ca.mohawk.jdw.shifty.clientapi.event.type.RemovePendingShiftChangeEvent;
import ca.mohawk.jdw.shifty.clientapi.model.PendingShiftChange;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public class RemovePendingShiftChangeEventMapper implements EventMapper<RemovePendingShiftChangeEvent> {

    @Override
    public RemovePendingShiftChangeEvent map(final ShiftyClient client, final Packet pkt){
        final PendingShiftChange pendingShiftChange = client.pendingShiftChanges().forId(pkt.readLong());
        if(pendingShiftChange == null)
            return null;
        return new RemovePendingShiftChangeEvent(
                pendingShiftChange
        );
    }
}

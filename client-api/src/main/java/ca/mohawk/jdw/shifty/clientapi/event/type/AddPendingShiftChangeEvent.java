package ca.mohawk.jdw.shifty.clientapi.event.type;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.Event;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;
import ca.mohawk.jdw.shifty.clientapi.model.PendingShiftChange;

public class AddPendingShiftChangeEvent extends Event {

    private final PendingShiftChange pendingShiftChange;

    public AddPendingShiftChangeEvent(final PendingShiftChange pendingShiftChange){
        super(EventType.ADD_PENDING_SHIFT_CHANGE);
        this.pendingShiftChange = pendingShiftChange;
    }

    public PendingShiftChange pendingShiftChange(){
        return pendingShiftChange;
    }
}

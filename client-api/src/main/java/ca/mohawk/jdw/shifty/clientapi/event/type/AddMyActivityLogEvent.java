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
import ca.mohawk.jdw.shifty.clientapi.model.ActivityLog;

public class AddMyActivityLogEvent extends Event {

    private final ActivityLog log;

    public AddMyActivityLogEvent(final ActivityLog log){
        super(EventType.ADD_MY_ACTIVITY_LOG);
        this.log = log;
    }

    public ActivityLog log(){
        return log;
    }
}

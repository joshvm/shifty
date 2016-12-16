package ca.mohawk.jdw.shifty.clientapi.event.response;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.Event;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;

public class Response extends Event {

    private final ResponseCode code;

    public Response(final EventType type,
                    final ResponseCode code){
        super(type);
        this.code = code;
    }

    public ResponseCode code(){
        return code;
    }
}

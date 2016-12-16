package ca.mohawk.jdw.shifty.server.model.shift;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.util.Map;

public enum RequestResponse {

    PENDING(0),
    ACCEPTED(1),
    DENIED(2);

    private static final Map<Integer, RequestResponse> MAP = Utils.map(values(), RequestResponse::id);

    private final int id;

    RequestResponse(final int id){
        this.id = id;
    }

    public int id(){
        return id;
    }

    public static RequestResponse forId(final int id){
        return MAP.get(id);
    }
}

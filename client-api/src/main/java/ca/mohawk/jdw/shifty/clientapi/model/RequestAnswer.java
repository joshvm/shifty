package ca.mohawk.jdw.shifty.clientapi.model;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.utils.Utils;
import java.util.Map;

public enum RequestAnswer {

    PENDING(0),
    ACCEPTED(1),
    DENIED(2);

    private static final Map<Integer, RequestAnswer> MAP = Utils.map(values(), RequestAnswer::id);

    private final int id;

    RequestAnswer(final int id){
        this.id = id;
    }

    public int id(){
        return id;
    }

    public static RequestAnswer forId(final int id){
        return MAP.getOrDefault(id, PENDING);
    }
}

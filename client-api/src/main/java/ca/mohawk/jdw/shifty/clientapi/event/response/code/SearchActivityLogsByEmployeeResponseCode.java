package ca.mohawk.jdw.shifty.clientapi.event.response.code;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import java.util.Map;

public enum SearchActivityLogsByEmployeeResponseCode implements ResponseCode {

    EMPLOYEE_NOT_FOUND(1),
    LOGS_NOT_FOUND(2);

    private static final Map<Integer, ResponseCode> MAP = ResponseCode.map(values());

    private final int id;

    SearchActivityLogsByEmployeeResponseCode(final int id){
        this.id = id;
    }

    @Override
    public int id(){
        return id;
    }

    public static ResponseCode forId(final int id){
        return ResponseCode.forId(MAP, id);
    }
}

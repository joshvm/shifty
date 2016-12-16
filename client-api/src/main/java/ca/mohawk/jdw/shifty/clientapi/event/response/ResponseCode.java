package ca.mohawk.jdw.shifty.clientapi.event.response;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.utils.Utils;
import java.util.Map;

public interface ResponseCode {

    ResponseCode SUCCESS = () -> 100;
    ResponseCode ERROR = () -> 101;
    ResponseCode UNKNOWN = () -> 102;

    int id();

    static Map<Integer, ResponseCode> map(final ResponseCode[] values){
        return Utils.map(values, ResponseCode::id);
    }

    static ResponseCode forId(final Map<Integer, ResponseCode> map,
                              final int id){
        switch(id){
            case 100:
                return SUCCESS;
            case 101:
                return ERROR;
            default:
                return map.getOrDefault(id, UNKNOWN);
        }
    }
}

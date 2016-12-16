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

public enum AnswerPendingShiftChangeResponseCode implements ResponseCode {

    /*
        private static final int NOT_FOUND = 1;
    private static final int INVALID_ANSWER = 2;
    private static final int NOT_A_MANAGER = 3;
    private static final int UNABLE_TO_FINALIZE = 4;
     */

    NOT_FOUND(1),
    INVALID_ANSWER(2),
    NOT_A_MANAGER(3),
    UNABLE_TO_FINALIZE(4);

    private static final Map<Integer, ResponseCode> MAP = ResponseCode.map(values());

    private final int id;

    AnswerPendingShiftChangeResponseCode(final int id){
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

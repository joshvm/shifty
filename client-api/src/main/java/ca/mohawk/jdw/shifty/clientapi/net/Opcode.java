package ca.mohawk.jdw.shifty.clientapi.net;

/*
  I, Josh Maione, 000320309 certify that this material is my original work.
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.utils.Utils;
import java.util.Map;

public enum Opcode {

    LOGIN(1, 0, PacketSize.VAR_BYTE), //
    LOGIN_RESPONSE(2, 1, null), //
    INIT(3, PacketSize.VAR_SHORT, null), //
    ADD_MY_SHIFT(4, PacketSize.VAR_SHORT, null), //
    REMOVE_MY_SHIFT(5, 8, null), //
    ADD_AVAILABLE_SHIFT(7, PacketSize.VAR_SHORT, null), //
    REMOVE_AVAILABLE_SHIFT(8, 8, null), //
    OFFER_MY_SHIFT(9, 0, PacketSize.FIXED),  //
    OFFER_MY_SHIFT_RESPONSE(10, 1, null), //
    CANCEL_MY_SHIFT_OFFER(11, 0, PacketSize.FIXED), //
    CANCEL_MY_SHIFT_OFFER_RESPONSE(12, 1, null), //
    ADD_MY_SHIFT_OFFER(13, 8, null), //shift_id(8) //
    REMOVE_MY_SHIFT_OFFER(14, 8, null), //shift_id(8) //
    REQUEST_TO_TAKE_SHIFT(15, 0, PacketSize.FIXED), //
    REQUEST_TO_TAKE_SHIFT_RESPONSE(16, 1, null), //
    CANCEL_REQUEST_TO_TAKE_SHIFT(17, 0, PacketSize.FIXED), //FIX THIS FOR SERVER OPCODE TOO!!!!!!!! //
    CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE(18, 1, null), //
    ADD_REQUEST_TO_MY_SHIFT_OFFER(19, PacketSize.VAR_SHORT, null), //
    REMOVE_REQUEST_FROM_MY_SHIFT_OFFER(20, 16, null), //
    ANSWER_REQUEST_FROM_MY_SHIFT_OFFER(21, 0, PacketSize.FIXED), //shift_id(8) + employee_id(8) + response_id(1) //
    ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE(22, 1, null), //
    UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER(23, 17, null), //
    ADD_MY_SHIFT_OFFER_REQUEST(24, PacketSize.VAR_SHORT, null), //
    REMOVE_MY_SHIFT_OFFER_REQUEST(25, 8, null), //
    UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE(26, 9, null), //
    ADD_MY_ACTIVITY_LOG(27, PacketSize.VAR_SHORT, null), //
    ADD_SEARCHED_ACTIVITY_LOG(28, PacketSize.VAR_SHORT, null), //
    ADD_PENDING_SHIFT_CHANGE(29, PacketSize.VAR_SHORT, null), //manager //
    REMOVE_PENDING_SHIFT_CHANGE(30, 8, null), //manager //
    ANSWER_PENDING_SHIFT_CHANGE(31, 0, PacketSize.FIXED), //manager //
    ANSWER_PENDING_SHIFT_CHANGE_RESPONSE(32, 1, null),
    REQUEST_COMPANY_INFO(33, 0, PacketSize.FIXED),
    INIT_COMPANY_INFO(34, PacketSize.VAR_SHORT, null),
    SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE(35, 0, PacketSize.VAR_BYTE),
    SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE(36, 1, null);

    private static final Map<Integer, Opcode> MAP = Utils.map(values(), Opcode::value);

    private final int value;
    private final int inLength;
    private final PacketSize inSize;
    private final PacketSize outSize;

    Opcode(final int value,
           final int inLength,
           final PacketSize outSize){
        this.value = value;
        this.inLength = inLength;
        this.outSize = outSize;

        inSize = PacketSize.forValue(inLength);
    }

    Opcode(final int value,
           final PacketSize inSize,
           final PacketSize outSize){
        this(value, inSize.value(), outSize);
    }

    public int value(){
        return value;
    }

    public int inLength(){
        return inLength;
    }

    public PacketSize inSize(){
        return inSize;
    }

    public PacketSize outSize(){
        return outSize;
    }

    public static Opcode forValue(final int value){
        return MAP.get(value);
    }
}

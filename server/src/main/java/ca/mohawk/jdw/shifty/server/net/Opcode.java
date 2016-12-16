package ca.mohawk.jdw.shifty.server.net;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.util.Map;

public enum Opcode {

    LOGIN(1, PacketSize.VAR_BYTE, null),
    LOGIN_RESPONSE(2, 0, PacketSize.FIXED),
    INIT(3, 0, PacketSize.VAR_SHORT),
    ADD_MY_SHIFT(4, 0, PacketSize.VAR_SHORT),
    REMOVE_MY_SHIFT(5, 0, PacketSize.FIXED),
    ADD_AVAILABLE_SHIFT(7, 0, PacketSize.VAR_SHORT),
    REMOVE_AVAILABLE_SHIFT(8, 0, PacketSize.FIXED),
    OFFER_MY_SHIFT(9, 8, null),
    OFFER_MY_SHIFT_RESPONSE(10, 0, PacketSize.FIXED),
    CANCEL_MY_SHIFT_OFFER(11, 8, null),
    CANCEL_MY_SHIFT_OFFER_RESPONSE(12, 0, PacketSize.FIXED),
    ADD_MY_SHIFT_OFFER(13, 0, PacketSize.FIXED), //shift_id(8)
    REMOVE_MY_SHIFT_OFFER(14, 0, PacketSize.FIXED), //shift_id(8)
    REQUEST_TO_TAKE_SHIFT(15, 8, null),
    REQUEST_TO_TAKE_SHIFT_RESPONSE(16, 0, PacketSize.FIXED),
    CANCEL_REQUEST_TO_TAKE_SHIFT(17, 8, null),
    CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE(18, 0, PacketSize.FIXED),
    ADD_REQUEST_TO_MY_SHIFT_OFFER(19, 0, PacketSize.VAR_SHORT),
    REMOVE_REQUEST_FROM_MY_SHIFT_OFFER(20, 0, PacketSize.FIXED),
    ANSWER_REQUEST_FROM_MY_SHIFT_OFFER(21, 17, null), //shift_id(8) + employee_id(8) + response_id(1)
    ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE(22, 0, PacketSize.FIXED),
    UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER(23, 0, PacketSize.FIXED),
    ADD_MY_SHIFT_OFFER_REQUEST(24, 0, PacketSize.VAR_SHORT),
    REMOVE_MY_SHIFT_OFFER_REQUEST(25, 0, PacketSize.FIXED),
    UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE(26, 0, PacketSize.FIXED),
    ADD_MY_ACTIVITY_LOG(27, 0, PacketSize.VAR_SHORT),
    ADD_SEARCHED_ACTIVITY_LOG(28, 0, PacketSize.VAR_SHORT),
    ADD_PENDING_SHIFT_CHANGE(29, 0, PacketSize.VAR_SHORT), //manager
    REMOVE_PENDING_SHIFT_CHANGE(30, 0, PacketSize.FIXED), //manager
    ANSWER_PENDING_SHIFT_CHANGE(31, 17, null), //manager
    ANSWER_PENDING_SHIFT_CHANGE_RESPONSE(32, 0, PacketSize.FIXED),
    REQUEST_COMPANY_INFO(33, 0, null),
    INIT_COMPANY_INFO(34, 0, PacketSize.VAR_SHORT),
    SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE(35, PacketSize.VAR_BYTE, null),
    SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE(36, 0, PacketSize.FIXED);

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
           final PacketSize inLength,
           final PacketSize outSize){
        this(value, inLength.value(), outSize);
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

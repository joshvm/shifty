package ca.mohawk.jdw.shifty.clientapi;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.Event;
import ca.mohawk.jdw.shifty.clientapi.event.EventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddSearchedActivityLogEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddAvailableShiftEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddMyActivityLogEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddMyShiftEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddMyShiftOfferEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddMyShiftOfferRequestEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddPendingShiftChangeEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.AddRequestToMyShiftOfferEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemoveAvailableShiftEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemoveMyShiftEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemoveMyShiftOfferEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemoveMyShiftOfferRequestEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemovePendingShiftChangeEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.RemoveRequestFromMyShiftOfferEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.UpdateMyShiftOfferRequestResponseEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.mapper.UpdateRequestResponseFromMyShiftOfferEventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseMapper;
import ca.mohawk.jdw.shifty.clientapi.event.response.code.AnswerPendingShiftChangeResponseCode;
import ca.mohawk.jdw.shifty.clientapi.event.response.code.AnswerRequestFromMyShiftOfferResponseCode;
import ca.mohawk.jdw.shifty.clientapi.event.response.code.CancelMyShiftOfferResponseCode;
import ca.mohawk.jdw.shifty.clientapi.event.response.code.OfferMyShiftResponseCode;
import ca.mohawk.jdw.shifty.clientapi.event.response.code.RequestToTakeShiftResponseCode;
import ca.mohawk.jdw.shifty.clientapi.net.Buffer;
import ca.mohawk.jdw.shifty.clientapi.net.Opcode;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.clientapi.utils.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class Connection implements Runnable {

    interface Listener {

        void onRead(final Event e);

        void onDisconnect();
    }

    private static final Map<Opcode, EventMapper> MAPPERS = new HashMap<>();

    static {
        //register response mappers
        MAPPERS.put(
                Opcode.OFFER_MY_SHIFT_RESPONSE,
                new ResponseMapper(EventType.OFFER_MY_SHIFT_RESPONSE, OfferMyShiftResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.REQUEST_TO_TAKE_SHIFT_RESPONSE,
                new ResponseMapper(EventType.REQUEST_TO_TAKE_SHIFT_RESPONSE, RequestToTakeShiftResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.CANCEL_MY_SHIFT_OFFER_RESPONSE,
                new ResponseMapper(EventType.CANCEL_MY_SHIFT_OFFER_RESPONSE, CancelMyShiftOfferResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE,
                new ResponseMapper(EventType.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, AnswerRequestFromMyShiftOfferResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE,
                new ResponseMapper(EventType.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, CancelMyShiftOfferResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE,
                new ResponseMapper(EventType.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, AnswerPendingShiftChangeResponseCode::forId)
        );
        MAPPERS.put(
                Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE,
                new ResponseMapper(EventType.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE, AnswerPendingShiftChangeResponseCode::forId)
        );

        //register event mappers
        MAPPERS.put(Opcode.ADD_MY_SHIFT, new AddMyShiftEventMapper());
        MAPPERS.put(Opcode.REMOVE_MY_SHIFT, new RemoveMyShiftEventMapper());
        MAPPERS.put(Opcode.ADD_AVAILABLE_SHIFT, new AddAvailableShiftEventMapper());
        MAPPERS.put(Opcode.ADD_MY_SHIFT_OFFER, new AddMyShiftOfferEventMapper());
        MAPPERS.put(Opcode.ADD_REQUEST_TO_MY_SHIFT_OFFER, new AddRequestToMyShiftOfferEventMapper());
        MAPPERS.put(Opcode.ADD_MY_SHIFT_OFFER_REQUEST, new AddMyShiftOfferRequestEventMapper());
        MAPPERS.put(Opcode.REMOVE_AVAILABLE_SHIFT, new RemoveAvailableShiftEventMapper());
        MAPPERS.put(Opcode.REMOVE_MY_SHIFT_OFFER, new RemoveMyShiftOfferEventMapper());
        MAPPERS.put(Opcode.REMOVE_REQUEST_FROM_MY_SHIFT_OFFER, new RemoveRequestFromMyShiftOfferEventMapper());
        MAPPERS.put(Opcode.REMOVE_MY_SHIFT_OFFER_REQUEST, new RemoveMyShiftOfferRequestEventMapper());
        MAPPERS.put(Opcode.ADD_MY_ACTIVITY_LOG, new AddMyActivityLogEventMapper());
        MAPPERS.put(Opcode.ADD_SEARCHED_ACTIVITY_LOG, new AddSearchedActivityLogEventMapper());
        MAPPERS.put(Opcode.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER, new UpdateRequestResponseFromMyShiftOfferEventMapper());
        MAPPERS.put(Opcode.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE, new UpdateMyShiftOfferRequestResponseEventMapper());
        MAPPERS.put(Opcode.ADD_PENDING_SHIFT_CHANGE, new AddPendingShiftChangeEventMapper());
        MAPPERS.put(Opcode.REMOVE_PENDING_SHIFT_CHANGE, new RemovePendingShiftChangeEventMapper());
    }

    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    protected boolean started;

    protected Listener listener;

    protected ShiftyClient client;

    Connection(final String host,
               final int port) throws IOException{
        socket = new Socket(host, port);

        out = new DataOutputStream(socket.getOutputStream());
        out.flush();

        in = new DataInputStream(socket.getInputStream());
    }

    void send(final Packet pkt) throws IOException {
        out.writeByte(pkt.opcode().value());
        switch(pkt.opcode().outSize()){
            case VAR_BYTE:
                out.writeByte(pkt.readableBytes());
                break;
            case VAR_SHORT:
                out.writeShort(pkt.readableBytes());
                break;
        }
        out.write(pkt.buf().array(), 0, pkt.readableBytes());
        out.flush();
        System.out.println("writing packet: " + pkt.opcode());
//        System.out.println("writing packet readable bytes: " + pkt.readableBytes());
    }

    Packet read() throws IOException {
        final Opcode opcode = Opcode.forValue(in.readUnsignedByte());
        if(opcode == null)
            return null;
        System.out.println("decoding packet opcode: " + opcode);
        int length = opcode.inLength();
        switch(opcode.inSize()){
            case VAR_BYTE:
                length = in.readUnsignedByte();
                break;
            case VAR_SHORT:
                length = in.readUnsignedShort();
                break;
        }
        final byte[] payload = new byte[length];
        in.readFully(payload);
        return new Packet(opcode, Buffer.wrap(payload));
    }

    Packet readForOpcode(final Opcode opcode) throws IOException {
        Packet pkt = read();
        while(pkt == null || pkt.opcode() != opcode)
            pkt = read();
        return pkt;
    }

    void disconnect() {
        Utils.close(out, true);
        Utils.close(in, true);
        Utils.close(socket, true);
    }

    boolean connected(){
        return socket.isConnected();
    }

    void startThread(){
        final Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        started = true;
    }

    @Override
    public void run(){
        while(true){
            try{
//                System.out.println("waiting for packet...");
                final Packet pkt = read();
//                System.out.println("received packet: " + pkt.opcode());
                final EventMapper mapper = MAPPERS.get(pkt.opcode());
                if(mapper == null){
                    System.out.println("no mapper available for : " + pkt.opcode());
                    continue;
                }
                final Event e = mapper.map(client, pkt);
                if(e != null) //could be an invalid packet or potentially out of sync...
                    listener.onRead(e);
            }catch(Exception ex){
                ex.printStackTrace();
                break;
            }
        }
        listener.onDisconnect();
    }
}

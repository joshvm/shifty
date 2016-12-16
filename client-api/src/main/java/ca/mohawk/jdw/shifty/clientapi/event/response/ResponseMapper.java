package ca.mohawk.jdw.shifty.clientapi.event.response;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.EventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import java.util.function.Function;

public class ResponseMapper implements EventMapper<Response> {

    private final EventType type;
    private final Function<Integer, ResponseCode> codeMethod;

    public ResponseMapper(final EventType type,
                          final Function<Integer, ResponseCode> codeMethod){
        this.type = type;
        this.codeMethod = codeMethod;
    }

    @Override
    public Response map(final ShiftyClient client, final Packet pkt){
        final int codeId = pkt.readUnsignedByte();
        return new Response(type, codeMethod.apply(codeId));
    }
}

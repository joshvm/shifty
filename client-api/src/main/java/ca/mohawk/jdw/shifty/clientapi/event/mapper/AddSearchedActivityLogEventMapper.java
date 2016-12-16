package ca.mohawk.jdw.shifty.clientapi.event.mapper;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.EventMapper;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddSearchedActivityLogEvent;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.clientapi.utils.NetUtils;

public class AddSearchedActivityLogEventMapper implements EventMapper<AddSearchedActivityLogEvent> {

    @Override
    public AddSearchedActivityLogEvent map(final ShiftyClient client, final Packet pkt){
        return new AddSearchedActivityLogEvent(
                NetUtils.deserializeActivityLog(
                        NetUtils.deserializeEmployee(pkt),
                        pkt
                )
        );
    }
}

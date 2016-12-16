package ca.mohawk.jdw.shifty.clientapi.event;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;

public interface EventMapper<E extends Event> {

    E map(final ShiftyClient client,
          final Packet pkt);
}

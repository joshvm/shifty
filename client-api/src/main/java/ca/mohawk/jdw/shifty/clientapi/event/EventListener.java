package ca.mohawk.jdw.shifty.clientapi.event;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;

public interface EventListener<E extends Event> {

    void onEvent(final ShiftyClient client,
                 final E e);
}

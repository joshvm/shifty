package ca.mohawk.jdw.shifty.clientapi.event;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

public class Event {

    private final EventType type;

    public Event(final EventType type){
        this.type = type;
    }

    public EventType type(){
        return type;
    }
}

package ca.mohawk.jdw.shifty.clientapi.event.type;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.Event;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;

public class UpdateRequestResponseFromMyShiftOfferEvent extends Event {

    private final ShiftOffer.Request request;
    private final RequestAnswer answer;

    public UpdateRequestResponseFromMyShiftOfferEvent(final ShiftOffer.Request request,
                                                      final RequestAnswer answer){
        super(EventType.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER);
        this.request = request;
        this.answer = answer;
    }

    public ShiftOffer.Request request(){
        return request;
    }

    public RequestAnswer answer(){
        return answer;
    }
}

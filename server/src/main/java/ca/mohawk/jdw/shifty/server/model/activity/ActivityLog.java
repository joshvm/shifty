package ca.mohawk.jdw.shifty.server.model.activity;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class ActivityLog {

    private final Employee employee;
    private final Timestamp timestamp;
    private final String message;

    public ActivityLog(final Employee employee,
                       final Timestamp timestamp,
                       final String message){
        this.employee = employee;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ActivityLog(final Employee employee,
                       final Timestamp timestamp,
                       final String fmt,
                       final Object... args){
        this(employee, timestamp, String.format(fmt, args));
    }

    public ActivityLog(final Employee employee,
                       final String fmt,
                       final Object... args){
        this(employee, Utils.timestamp(), fmt, args);
    }

    public Employee employee(){
        return employee;
    }

    public Timestamp timestamp(){
        return timestamp;
    }

    public String message(){
        return message;
    }

    public static ActivityLog offerMyShift(final ShiftOffer offer){
        return new ActivityLog(
                offer.employee(),
                "Offered Shift %d: %s %s to %s %s",
                offer.id(),
                offer.shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                offer.shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                offer.shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                offer.shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog cancelMyShiftOffer(final ShiftOffer offer){
        return new ActivityLog(
                offer.employee(),
                "Cancelled Shift Offer %d: %s %s to %s %s",
                offer.id(),
                offer.shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                offer.shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                offer.shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                offer.shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog requestToTakeShift(final ShiftOfferRequest request){
        return new ActivityLog(
                request.employee(),
                "Requested To Take Shift %d: %s %s to %s %s By %s %s",
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().employee().firstName(),
                request.offer().employee().lastName()
        );
    }

    public static ActivityLog requestAddedToMyShiftOffer(final ShiftOfferRequest request){
        return new ActivityLog(
                request.offer().employee(),
                "%s %s Requested To Take Your Shift %d: %s %s to %s %s",
                request.employee().firstName(),
                request.employee().lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog shiftOfferCancelled(final ShiftOfferRequest request){
        return new ActivityLog(
                request.employee(),
                "%s %s Has Cancelled Their Shift Offer %d: %s %s to %s %s",
                request.offer().employee().firstName(),
                request.offer().employee().lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog cancelRequestToTakeShift(final ShiftOfferRequest request){
        return new ActivityLog(
                request.employee(),
                "Cancelled Request To Take Shift %d: %s %s to %s %s By %s %s",
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().employee().firstName(),
                request.offer().employee().lastName()
        );
    }

    public static ActivityLog requestCancelledFromMyShiftOffer(final ShiftOfferRequest request){
        return new ActivityLog(
                request.offer().employee(),
                "%s %s Cancelled Their Request To Take Your Shift %d: %s %s to %s %s",
                request.employee().firstName(),
                request.employee().lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog answerRequestFromMyShiftOffer(final ShiftOfferRequest request,
                                                            final RequestResponse response){
        return new ActivityLog(
                request.offer().employee(),
                "%s Request To Take Shift %d: %s %s to %s %s By %s %s",
                response,
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.employee().firstName(),
                request.employee().lastName(),
                response
        );
    }

    public static ActivityLog updateMyShiftOfferRequestResponse(final ShiftOfferRequest request,
                                                                final RequestResponse response){
        return new ActivityLog(
                request.employee(),
                "%s %s %s Your Request To Take Their Shift %d: %s %s to %s %s",
                request.offer().employee().firstName(),
                request.offer().employee().lastName(),
                response,
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog answeredPendingShiftChange(final Employee manager,
                                                         final ShiftOfferRequest request,
                                                         final RequestResponse response){
        return new ActivityLog(
                manager,
                "%s Request For %s %s To Take Shift %d: %s %s to %s %s By %s %s",
                response,
                request.employee().firstName(),
                request.employee().lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().employee().firstName(),
                request.offer().employee().lastName()
        );
    }

    public static ActivityLog pendingShiftChangeAnsweredForOfferingEmployee(final Employee manager,
                                                                            final ShiftOfferRequest request,
                                                                            final RequestResponse response){
        return new ActivityLog(
                request.offer().employee(),
                "Request %s By %s %s For %s %s To Take Your Shift %d: %s %s to %s %s",
                response,
                manager.firstName(),
                manager.lastName(),
                request.employee().firstName(),
                request.employee().lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
        );
    }

    public static ActivityLog pendingShiftChangeAnsweredForRequestingEmployee(final Employee manager,
                                                                              final ShiftOfferRequest request,
                                                                              final RequestResponse response){
        return new ActivityLog(
                request.employee(),
                "Request %s By %s %s To Take Shift %d: %s %s to %s %s By %s %s",
                response,
                manager.firstName(),
                manager.lastName(),
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().employee().firstName(),
                request.offer().employee().lastName()
        );
    }

    public static ActivityLog shiftOfferNoLongerAvailable(final ShiftOfferRequest request){
        return new ActivityLog(
                request.employee(),
                "Shift Offer %d: %s %s to %s %s By %s %s Is No Longer Available",
                request.offer().id(),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().startTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                request.offer().shift().finishTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                request.offer().employee().firstName(),
                request.offer().employee().lastName()
        );
    }
}

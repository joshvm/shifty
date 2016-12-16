package ca.mohawk.jdw.shifty.clientapi.model;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/


import ca.mohawk.jdw.shifty.companyapi.model.Model;
import ca.mohawk.jdw.shifty.companyapi.model.ModelManager;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import javafx.beans.property.SimpleObjectProperty;

public class ShiftOffer implements Model {

    public static class Request implements Model {

        private final ShiftOffer offer;
        private final Employee employee;
        private final SimpleObjectProperty<RequestAnswer> answer;

        public Request(final ShiftOffer offer,
                       final Employee employee,
                       final RequestAnswer answer){
            this.offer = offer;
            this.employee = employee;
            this.answer = new SimpleObjectProperty<>(answer);
        }

        @Override
        public long id(){
            return offer.id();
        }

        public ShiftOffer offer(){
            return offer;
        }

        public Employee employee(){
            return employee;
        }

        public SimpleObjectProperty<RequestAnswer> answerProperty(){
            return answer;
        }

        public RequestAnswer answer(){
            return answer.get();
        }

        public void answer(final RequestAnswer answer){
            this.answer.set(answer);
        }
    }

    private final Employee offeringEmployee;
    private final Shift shift;

    private final ModelManager<Request> requests;

    public ShiftOffer(final Employee offeringEmployee,
                      final Shift shift){
        this.offeringEmployee = offeringEmployee;
        this.shift = shift;

        requests = new ModelManager<>();
    }

    @Override
    public long id(){
        return shift.id();
    }

    public Employee offeringEmployee(){
        return offeringEmployee;
    }

    public Shift shift(){
        return shift;
    }

    public ModelManager<Request> requests(){
        return requests;
    }
}

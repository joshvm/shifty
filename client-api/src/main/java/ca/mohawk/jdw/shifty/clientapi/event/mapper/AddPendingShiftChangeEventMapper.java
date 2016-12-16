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
import ca.mohawk.jdw.shifty.clientapi.event.type.AddPendingShiftChangeEvent;
import ca.mohawk.jdw.shifty.clientapi.model.PendingShiftChange;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.clientapi.utils.NetUtils;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;

public class AddPendingShiftChangeEventMapper implements EventMapper<AddPendingShiftChangeEvent> {

    @Override
    public AddPendingShiftChangeEvent map(final ShiftyClient client, final Packet pkt){
        final Employee offeringEmployee = NetUtils.deserializeEmployee(pkt);
        final Shift shift = NetUtils.deserializeShift(offeringEmployee.id(), pkt);
        final Employee requestingEmployee = NetUtils.deserializeEmployee(pkt);
        return new AddPendingShiftChangeEvent(
                new PendingShiftChange(
                        offeringEmployee,
                        shift,
                        requestingEmployee
                )
        );
    }
}

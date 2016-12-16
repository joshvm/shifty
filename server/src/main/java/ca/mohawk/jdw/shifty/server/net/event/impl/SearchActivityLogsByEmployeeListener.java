package ca.mohawk.jdw.shifty.server.net.event.impl;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.ClientPacketListener;
import java.util.Collection;


import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class SearchActivityLogsByEmployeeListener implements ClientPacketListener {

    private static final int EMPLOYEE_NOT_FOUND = 1;
    private static final int LOGS_NOT_FOUND = 2;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final Client client, final Packet pkt) throws Exception{
        final String employeeUser = pkt.readString();
        final Employee employee = company.employees().forUser(employeeUser);
        if(employee == null){
            client.writeResponse(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE, EMPLOYEE_NOT_FOUND);
            return;
        }
        final Collection<ActivityLog> logs = database.activityLogs().forEmployee(employee.id());
        if(logs == null){
            client.writeResponse(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE, LOGS_NOT_FOUND);
            return;
        }
        client.writeResponse(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE, SUCCESS);
        client.addSearchedActivityLogs(logs);
    }

    @Override
    public void onError(final Client client, final Packet pkt, final Throwable err){
        err.printStackTrace();
        client.writeResponse(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE, ERROR);
    }
}

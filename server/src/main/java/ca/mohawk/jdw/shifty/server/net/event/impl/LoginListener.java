package ca.mohawk.jdw.shifty.server.net.event.impl;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.PacketListener;
import ca.mohawk.jdw.shifty.server.net.utils.NetUtils;
import io.netty.channel.ChannelHandlerContext;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;
import static ca.mohawk.jdw.shifty.server.Shifty.company;

public class LoginListener implements PacketListener {

    private static final int ALREADY_LOGGED_IN = 1;
    private static final int EMPLOYEE_NOT_FOUND = 2;
    private static final int PASS_MISMATCH = 3;
    private static final int EMPLOYEE_ALREADY_ACTIVE = 4;

    private static final int SUCCESS = 100;
    private static final int ERROR = 101;

    @Override
    public void onPacket(final ChannelHandlerContext ctx, final Packet pkt) throws Exception{
        if(ctx.channel().hasAttr(Client.CLIENT_KEY)){
            NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, ALREADY_LOGGED_IN);
            return;
        }
        final String userOrId = pkt.readString();
        final String pass = pkt.readString();
        final Employee employee = userOrId.matches("\\d{1,9}")
                ? company.employees().forId(Long.parseLong(userOrId))
                : company.employees().forUser(userOrId);
        if(employee == null){
            NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, EMPLOYEE_NOT_FOUND);
            return;
        }
        if(!company.employeeValidator().isPassword(employee, pass)){
            NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, PASS_MISMATCH);
            return;
        }
        if(clients.forId(employee.id()) != null){
            NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, EMPLOYEE_ALREADY_ACTIVE);
            return;
        }
        final Client client = Client.register(ctx, employee);
        clients.add(client);
        NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, SUCCESS);
        client.init();
        client.addMyShifts();
        client.addAvailableShifts();
        client.addMyShiftOffers();
        client.addMyShiftOfferRequests();
        client.addMyActivityLogs();
        if(client.employee().isManager())
            client.addPendingShiftChanges();
    }

    @Override
    public void onError(final ChannelHandlerContext ctx, final Packet pkt, final Throwable err){
        err.printStackTrace();
        NetUtils.writeResponse(ctx, Opcode.LOGIN_RESPONSE, ERROR);
    }
}

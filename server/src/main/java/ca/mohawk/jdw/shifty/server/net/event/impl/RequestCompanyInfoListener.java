package ca.mohawk.jdw.shifty.server.net.event.impl;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.event.PacketListener;
import ca.mohawk.jdw.shifty.server.net.utils.NetUtils;
import io.netty.channel.ChannelHandlerContext;


import static ca.mohawk.jdw.shifty.server.Shifty.company;

public class RequestCompanyInfoListener implements PacketListener {

    @Override
    public void onPacket(final ChannelHandlerContext ctx, final Packet pkt) throws Exception{
        ctx.writeAndFlush(new Packet(Opcode.INIT_COMPANY_INFO, NetUtils.serialize(company)));
    }
}

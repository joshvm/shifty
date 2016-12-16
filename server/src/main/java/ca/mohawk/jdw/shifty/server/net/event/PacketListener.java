package ca.mohawk.jdw.shifty.server.net.event;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.net.Packet;
import io.netty.channel.ChannelHandlerContext;

public interface PacketListener {

    void onPacket(final ChannelHandlerContext ctx,
                  final Packet pkt) throws Exception;

    default void onError(final ChannelHandlerContext ctx,
                 final Packet pkt,
                 final Throwable err){
        err.printStackTrace();
    }
}

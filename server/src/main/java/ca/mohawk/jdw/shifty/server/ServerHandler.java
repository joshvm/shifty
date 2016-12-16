package ca.mohawk.jdw.shifty.server;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.client.Client;
import ca.mohawk.jdw.shifty.server.net.event.PacketListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.AnswerPendingShiftChangeListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.AnswerRequestFromMyShitOfferListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.CancelMyShiftOfferListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.CancelRequestToTakeShiftListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.LoginListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.OfferMyShiftListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.RequestCompanyInfoListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.RequestToTakeShiftListener;
import ca.mohawk.jdw.shifty.server.net.event.impl.SearchActivityLogsByEmployeeListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashMap;
import java.util.Map;


import static ca.mohawk.jdw.shifty.server.Shifty.clients;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Opcode, PacketListener> LISTENERS = new HashMap<>();

    static {
        LISTENERS.put(Opcode.REQUEST_COMPANY_INFO, new RequestCompanyInfoListener());
        LISTENERS.put(Opcode.LOGIN, new LoginListener());
        LISTENERS.put(Opcode.OFFER_MY_SHIFT, new OfferMyShiftListener());
        LISTENERS.put(Opcode.CANCEL_MY_SHIFT_OFFER, new CancelMyShiftOfferListener());
        LISTENERS.put(Opcode.REQUEST_TO_TAKE_SHIFT, new RequestToTakeShiftListener());
        LISTENERS.put(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT, new CancelRequestToTakeShiftListener());
        LISTENERS.put(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER, new AnswerRequestFromMyShitOfferListener());
        LISTENERS.put(Opcode.ANSWER_PENDING_SHIFT_CHANGE, new AnswerPendingShiftChangeListener());
        LISTENERS.put(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE, new SearchActivityLogsByEmployeeListener());
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception{

    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception{
        final Packet pkt = (Packet) msg;
        final PacketListener listener = LISTENERS.get(pkt.opcode());
        if(listener == null){
            System.err.println("No listener for: " + pkt.opcode());
            return;
        }
        try{
            System.out.println("handling packet: " + pkt.opcode());
            listener.onPacket(ctx, pkt);
        }catch(Exception ex){
            listener.onError(ctx, pkt, ex);
            throw ex;
        }finally{
            pkt.buf().release();
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception{
        if(!ctx.channel().hasAttr(Client.CLIENT_KEY))
            return;
        final Client client = ctx.channel().attr(Client.CLIENT_KEY).getAndSet(null);
        clients.remove(client);
    }
}

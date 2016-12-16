package ca.mohawk.jdw.shifty.server.net.utils;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.net.Buffer;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;

public final class NetUtils {

    private NetUtils(){}

    public static String ip(final ChannelHandlerContext ctx){
        return ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString();
    }

    public static ChannelFuture writeLater(final ChannelHandlerContext ctx,
                                           final Packet pkt){
        return ctx.write(pkt);
    }

    public static ChannelFuture write(final ChannelHandlerContext ctx,
                                         final Packet pkt){
        return ctx.writeAndFlush(pkt);
    }

    public static ChannelFuture writeResponse(final ChannelHandlerContext ctx,
                                              final Opcode opcode,
                                              final int response){
        return write(ctx, new Packet(opcode).writeByte(response));
    }

    public static Buffer serialize(final Company company){
        return new Buffer()
                .writeString(company.name())
                .writeString(company.email())
                .writeString(company.phone())
                .writeInt(company.address().unit())
                .writeString(company.address().street())
                .writeString(company.address().city())
                .writeString(company.address().country());
    }

    public static Buffer serialize(final Employee employee){
        return new Buffer()
                .writeLong(employee.id())
                .writeString(employee.user())
                .writeByte(employee.rank().id())
                .writeString(employee.firstName())
                .writeString(employee.lastName())
                .writeString(employee.description());
    }

    public static Buffer serialize(final Shift shift){
        return new Buffer()
                .writeLong(shift.id())
                .writeLong(shift.startTimestamp().getTime())
                .writeLong(shift.finishTimestamp().getTime())
                .writeString(shift.description());
    }

    public static Buffer serialize(final ActivityLog log){
        return new Buffer()
                .writeLong(log.timestamp().getTime())
                .writeString(log.message());
    }

}

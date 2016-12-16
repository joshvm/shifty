package ca.mohawk.jdw.shifty.clientapi.net;

/*
  I, Josh Maione, 000320309 certify that this material is my original work.
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Buffer {

    private static final char STRING_TERMINATOR = 0;

    private final ByteBuf buf;

    public Buffer(final ByteBuf buf){
        this.buf = buf;
    }

    public Buffer(){
        this(Unpooled.buffer());
    }

    public ByteBuf buf(){
        return buf;
    }

    public int readableBytes(){
        return buf.readableBytes();
    }

    public Buffer writeBuffer(final Buffer b){
        buf.writeBytes(b.buf);
        return this;
    }

    public byte readByte(){
        return buf.readByte();
    }

    public Buffer writeByte(final int b){
        buf.writeByte(b);
        return this;
    }

    public short readUnsignedByte(){
        return buf.readUnsignedByte();
    }

    public char readChar(){
        return buf.readChar();
    }

    public Buffer writeChar(final char c){
        buf.writeChar(c);
        return this;
    }

    public short readShort(){
        return buf.readShort();
    }

    public Buffer writeShort(final int s){
        buf.writeShort(s);
        return this;
    }

    public int readUnsignedShort(){
        return buf.readUnsignedShort();
    }

    public int readInt(){
        return buf.readInt();
    }

    public Buffer writeInt(final int i){
        buf.writeInt(i);
        return this;
    }

    public long readLong(){
        return buf.readLong();
    }

    public Buffer writeLong(final long l){
        buf.writeLong(l);
        return this;
    }

    public String readString(){
        final StringBuilder bldr = new StringBuilder();
        char c;
        while((c = readChar()) != STRING_TERMINATOR)
            bldr.append(c);
        return bldr.toString();
    }

    public Buffer writeString(final String s){
        for(final char c : s.toCharArray())
            writeChar(c);
        return writeChar(STRING_TERMINATOR);
    }

    public Buffer writeBytes(final byte[] bytes){
        buf.writeBytes(bytes);
        return this;
    }

    public static Buffer wrap(final byte[] bytes){
        return new Buffer()
                .writeBytes(bytes);
    }
}

package ca.mohawk.jdw.shifty.clientapi.net;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

public class Packet extends Buffer {

    private final Opcode opcode;

    public Packet(final Opcode opcode){
        this.opcode = opcode;
    }

    public Packet(final Opcode opcode,
                  final Buffer payload){
        this(opcode);
        writeBuffer(payload);
    }

    public Opcode opcode(){
        return opcode;
    }

    @Override
    public Packet writeBuffer(final Buffer b){
        super.writeBuffer(b);
        return this;
    }

    @Override
    public Packet writeByte(final int b){
        super.writeByte(b);
        return this;
    }

    @Override
    public Packet writeChar(final char c){
        super.writeChar(c);
        return this;
    }

    @Override
    public Packet writeShort(final int s){
        super.writeShort(s);
        return this;
    }

    @Override
    public Packet writeInt(final int i){
        super.writeInt(i);
        return this;
    }

    @Override
    public Packet writeLong(final long l){
        super.writeLong(l);
        return this;
    }

    @Override
    public Packet writeString(final String s){
        super.writeString(s);
        return this;
    }

    @Override
    public Packet writeBytes(final byte[] bytes){
        super.writeBytes(bytes);
        return this;
    }
}

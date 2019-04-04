package top.mengtech.nettyim.protocol.packet;

import lombok.Data;

import static top.mengtech.nettyim.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet{
    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }

    private String message;
}

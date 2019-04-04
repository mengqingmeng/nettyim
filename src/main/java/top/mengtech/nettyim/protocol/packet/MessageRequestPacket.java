package top.mengtech.nettyim.protocol.packet;

import lombok.Data;

import static top.mengtech.nettyim.protocol.command.Command.MESSAGE_REQUEST;

// 消息请求类
@Data
public class MessageRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }

    private String message;
}

package top.mengtech.nettyim.protocol.packet;

import lombok.Data;
import top.mengtech.nettyim.protocol.packet.Packet;

import static top.mengtech.nettyim.protocol.command.Command.LOGIN_REQUEST;

/**
 * 登陆请求数据包
 */
@Data
public class LoginRequestPacket extends Packet {
    private String userId;
    private String username;
    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}

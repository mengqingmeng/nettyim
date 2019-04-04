package top.mengtech.nettyim.protocol.packet;

import lombok.Data;

/**
 * 数据包抽象类
 */
@Data
public abstract class Packet {

    // 默认版本
    private Byte version = 1;

    // 指令，比如是请求，还是响应之类的
    public abstract Byte getCommand();
}

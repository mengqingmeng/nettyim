package top.mengtech.nettyim.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import top.mengtech.nettyim.protocol.packet.*;
import top.mengtech.nettyim.protocol.serializer.JSONSerializer;
import top.mengtech.nettyim.protocol.serializer.Serializer;

import java.util.HashMap;
import java.util.Map;

import static top.mengtech.nettyim.protocol.command.Command.*;

/**
 * packet 编码、解码工具类
 */
public class PacketCodeC {

    private static final int MAGIC_NUMBER = 0x12345678;

    public static final PacketCodeC INSTANCE = new PacketCodeC();

    // 请求包类型
    private final Map<Byte,Class<? extends Packet>> packetTypeMap;

    // 序列化方法Map
    private final Map<Byte, Serializer> serializerMap;

    // 私有构造，初始化packet类型Map和序列化算法Map
    private PacketCodeC() {
        packetTypeMap = new HashMap<>();

        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(),serializer);

    }

    /**
     * 获取序列化算法
     * @param serializeAlgorithm
     * @return
     */
    private Serializer getSerializer(byte serializeAlgorithm){
        return serializerMap.get(serializeAlgorithm);
    }

    /**
     * 获取请求类型
     * @param command
     * @return
     */
    private Class<? extends Packet> getRequestType(byte command){
        return packetTypeMap.get(command);
    }

    /**
     * 编码
     * @param byteBuf
     * @param packet
     * @return
     */
    public ByteBuf encode(ByteBuf byteBuf,Packet packet){

        // 序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 编码数据
        byteBuf.writeInt(MAGIC_NUMBER);// 魔数
        byteBuf.writeByte(packet.getVersion());//版本
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm()); //序列化算法
        byteBuf.writeByte(packet.getCommand()); //请求数据包类型
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf){
        byteBuf.skipBytes(4);// 跳过魔数
        byteBuf.skipBytes(1);// 跳过版本
        // 序列化算法
        byte serializerAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 长度
        int length = byteBuf.readInt();
        // 数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 反序列化
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);
        if (requestType!=null && serializer !=null){
            return serializer.deserialize(requestType,bytes);
        }
        return null;
    }
}

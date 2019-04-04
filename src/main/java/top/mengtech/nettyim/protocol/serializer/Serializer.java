package top.mengtech.nettyim.protocol.serializer;

// 序列化接口
public interface Serializer {

    // 序列化算法
    byte getSerializerAlgorithm();

    // 编码，java对象转二进制
    byte[] serialize(Object object);

    // 解码，二进制转 java 对象
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * 默认使用 json 序列化
     */
    Serializer DEFAULT = new JSONSerializer();

}

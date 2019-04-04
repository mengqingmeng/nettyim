package top.mengtech.nettyim.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.mengtech.nettyim.protocol.packet.LoginRequestPacket;
import top.mengtech.nettyim.protocol.packet.LoginResponsePacket;
import top.mengtech.nettyim.protocol.packet.MessageResponsePacket;
import top.mengtech.nettyim.protocol.packet.Packet;
import top.mengtech.nettyim.utils.LoginUtil;
import top.mengtech.nettyim.utils.PacketCodeC;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    // 客户端连接建立成功之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(new Date()+":客户端发出数据");

        // 构建登陆数据
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("test");
        loginRequestPacket.setPassword("test123");

        // 编码
        ByteBuf loginByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(),loginRequestPacket);

        ctx.channel().writeAndFlush(loginByteBuf);
    }

    // 读取服务端写回的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");

                // 标记登陆成功
                Channel channel = ctx.channel();
                LoginUtil.markAsLogin(channel);
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        }else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }
}

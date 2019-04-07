package top.mengtech.nettyim.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.mengtech.nettyim.protocol.packet.MessageRequestPacket;
import top.mengtech.nettyim.protocol.packet.MessageResponsePacket;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {

        System.out.println(new Date() + "：收到客户端消息："+msg.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复【" + msg.getMessage() + "】");
        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}

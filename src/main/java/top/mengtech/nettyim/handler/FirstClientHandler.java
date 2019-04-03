package top.mengtech.nettyim.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    // 客户端连接建立成功之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(new Date()+":客户端发出数据");

        ByteBuf byteBuf = getByteBuf(ctx);
        ctx.channel().writeAndFlush(byteBuf);
    }

    // 读取服务端写回的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端收到数据：" + byteBuf.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        ByteBuf byteBuf = ctx.alloc().buffer();

        byte[] bytes = "你好在吗？".getBytes(Charset.forName("utf-8"));

        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}

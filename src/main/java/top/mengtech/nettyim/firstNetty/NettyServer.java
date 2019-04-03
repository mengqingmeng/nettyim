package top.mengtech.nettyim.firstNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {

    public static void main(String[] args) {
        // 引导类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 接活，获取新的连接的线程组
        NioEventLoopGroup parent = new NioEventLoopGroup();
        // 干活，负责数据读写的线程组
        NioEventLoopGroup child = new NioEventLoopGroup();

        serverBootstrap
                .group(parent,child)    // 线程模型
                .channel(NioServerSocketChannel.class)  // 指定服务端IO模型
                .childHandler(new ChannelInitializer<NioSocketChannel>() {  // 读写处理逻辑
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println("id:"+nioSocketChannel.id()+",msg:"+s);
                            }
                        });
                    }
                })
                .bind(8000);    // 绑定端口
    }
}

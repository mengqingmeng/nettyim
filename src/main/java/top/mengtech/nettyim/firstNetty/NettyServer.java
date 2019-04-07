package top.mengtech.nettyim.firstNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.mengtech.nettyim.handler.*;
import top.mengtech.nettyim.utils.PacketDecoder;
import top.mengtech.nettyim.utils.PacketEncoder;

public class NettyServer {

    private static final int PORT = 8000;

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
                .handler(new ChannelInitializer<NioServerSocketChannel>() { // 服务端启动的逻辑
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        System.out.println("服务端启动中。。。");
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {  // 通道连接时候的逻辑，读写处理逻辑
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

//                        nioSocketChannel.pipeline().addLast(new ServerHandler());
                        nioSocketChannel.pipeline().addLast(new PacketDecoder());   // 解码

                        nioSocketChannel.pipeline().addLast(new LoginRequestHandler());
                        nioSocketChannel.pipeline().addLast(new MessageRequestHandler());

                        nioSocketChannel.pipeline().addLast(new PacketEncoder());   // 编码
                    }
                });
                //.bind(8000);    // 绑定端口

        bind(serverBootstrap,PORT);

    }

    // 动态绑定端口，若失败，则不断去尝试
    private static void bind(final ServerBootstrap serverBootstrap,final int initPort){
        serverBootstrap.bind(initPort).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("端口[" + initPort + "]绑定成功!");
                } else {
                    System.err.println("端口[" + initPort + "]绑定失败!");
                    bind(serverBootstrap, initPort + 1);
                }
            }
        });
    }
}

package top.mengtech.nettyim.firstNetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.mengtech.nettyim.handler.ClientHandler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 连接超时参数
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
//                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                        nioSocketChannel.pipeline().addLast(new ClientHandler());
                    }
                });

        connect(bootstrap,HOST,PORT,MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap,String host,int port,int retry){
        bootstrap.connect(host,port).addListener(future -> {
           if (future.isSuccess()){
               System.out.println("客户端连接成功");
           }else if (retry == 0){
               System.err.println("重试次数已用完，放弃连接");
           }else{
               int order = (MAX_RETRY - retry) + 1;
               int delay = 1 << order;
               System.err.println(new Date()+":连接失败，第" + order + "次重连。。。");

               // 重连计划
               bootstrap
                       .config()
                       .group()
                       .schedule(
                               ()-> connect(bootstrap,host,port,retry-1),
                               delay,
                               TimeUnit.SECONDS);
           }
        });
    }
}

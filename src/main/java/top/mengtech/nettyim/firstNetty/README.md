### 客户端和服务端的启动

- 服务端
    - 引导类 `ServerBootstrap`
    - 线程模型 `NioEventLoopGroup`
    - 指定服务端IO模型 `NioServerSocketChannel.class`
    - 读数据
    - 动态绑定端口
    - 自定义属性
    - 设置***每个连接***TCP底层`serverBootstrap.childOption(ChannelOption option)`
    - 设置***服务端***属性`serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
`
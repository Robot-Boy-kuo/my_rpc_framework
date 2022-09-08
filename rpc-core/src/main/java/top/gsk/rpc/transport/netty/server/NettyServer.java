package top.gsk.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.enumeration.RpcError;
import top.gsk.rpc.exception.RpcException;
import top.gsk.rpc.hook.ShutdownHook;
import top.gsk.rpc.provider.ServiceProvider;
import top.gsk.rpc.provider.ServiceProviderImpl;
import top.gsk.rpc.registry.NacosServiceRegistry;
import top.gsk.rpc.registry.ServiceRegistry;
import top.gsk.rpc.serializer.CommonSerializer;
import top.gsk.rpc.transport.RpcServer;
import top.gsk.rpc.codec.CommonDecoder;
import top.gsk.rpc.codec.CommonEncoder;
import top.gsk.rpc.serializer.KryoSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static top.gsk.rpc.serializer.CommonSerializer.DEFAULT_SERIALIZER;

/**
 * @author gsk
 * @version 1.0
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;
    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }


    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);//向ConcurrentHashMap中存放k-v，k：HelloService需要重写的方法类，暴露给客户端，可以调用 v：HelloServiceImpl服务实现类，用于客户端实现
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();//TODO
        EventLoopGroup bossGroup = new NioEventLoopGroup();//负责连接的建立
        EventLoopGroup workerGroup = new NioEventLoopGroup();//负责处理数据收发
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)//TODO 此处参数代表啥
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();//TODO
                            //IdleStateHandler，设置一下客户端的写空闲时间
                            //当客户端的所有ChannelHandler中30s内没有write事件，则会触发userEventTriggered方法
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new CommonEncoder(serializer))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();//关闭当前ChannelFuture，同步关闭

        }catch (InterruptedException e){
            logger.error("启动服务器时有错误发生: ", e);
        }finally {//保证在try-catch中必须执行的操作
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }



    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}

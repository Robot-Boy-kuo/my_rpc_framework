package top.gsk.rpc.transport.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.entity.RpcRequest;
import top.gsk.rpc.entity.RpcResponse;
import top.gsk.rpc.enumeration.RpcError;
import top.gsk.rpc.exception.RpcException;
import top.gsk.rpc.factory.SingletonFactory;
import top.gsk.rpc.loadbalancer.LoadBalancer;
import top.gsk.rpc.loadbalancer.RandomLoadBalancer;
import top.gsk.rpc.registry.NacosServiceDiscovery;
import top.gsk.rpc.registry.NacosServiceRegistry;
import top.gsk.rpc.registry.ServiceDiscovery;
import top.gsk.rpc.registry.ServiceRegistry;
import top.gsk.rpc.serializer.CommonSerializer;
import top.gsk.rpc.transport.RpcClient;
import top.gsk.rpc.util.RpcMessageChecker;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static top.gsk.rpc.serializer.CommonSerializer.DEFAULT_SERIALIZER;

/**
 * @author gsk
 * @version 1.0
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup group;


    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }

    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("?????????????????????");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
//        AtomicReference<Object> result = new AtomicReference<>(null);//TODO ??????????????????????????????
//        try {
//            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
//            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
//            if (channel.isActive()) {
//                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
//                    if (future1.isSuccess()) {
//                        logger.info(String.format("?????????????????????: %s", rpcRequest.toString()));
//                    } else {
//                        logger.error("??????????????????????????????: ", future1.cause());
//                    }
//                });
//                channel.closeFuture().sync();
//                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
//                RpcResponse rpcResponse = channel.attr(key).get();
//                RpcMessageChecker.check(rpcRequest, rpcResponse);
//                result.set(rpcResponse.getData());
//            } else {
//                System.exit(0);
//            }
//        } catch (InterruptedException e) {
//            logger.error("??????????????????????????????: ", e);
//        }
//        return result.get();


        //TODO CompletableFuture
        /*
        * ??????completetableFuture????????????????????????
        * ?????????????????????????????????Future?????????????????????????????????????????? CompletionStage ???????????????????????????????????????????????????????????????????????????
        *
        * */
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info(String.format("?????????????????????: %s", rpcRequest.toString()));
                } else {
                    future1.channel().close();
                    //??????????????????????????????
                    resultFuture.completeExceptionally(future1.cause());
                    logger.error("??????????????????????????????: ", future1.cause());
                }
            });
        } finally {

        }

//        } catch (InterruptedException e) {
//            unprocessedRequests.remove(rpcRequest.getRequestId());
//            logger.error(e.getMessage(), e);
//            Thread.currentThread().interrupt();

        //NettyClientHandler??????????????????????????????rpcResponse
        return resultFuture;
    }

}

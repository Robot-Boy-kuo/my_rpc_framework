package top.gsk.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.factory.SingletonFactory;
import top.gsk.rpc.handler.RequestHandler;
import top.gsk.rpc.entity.RpcRequest;
import top.gsk.rpc.entity.RpcResponse;
import top.gsk.rpc.factory.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author gsk
 * @version 1.0
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    //private static final ExecutorService threadPool;


    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

//    static {
//        requestHandler = new RequestHandler();
//        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        //threadPool.execute(() -> {
            try {
                //logger.info("服务器接收到请求: {}", msg);
//                Object result = requestHandler.handle(msg);
//                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
//                future.addListener(ChannelFutureListener.CLOSE);
                //收到心跳包
                if(msg.getHeartBeat()) {
                    logger.info("接收到客户端心跳包...");
                    return;
                }
                logger.info("服务器接收到请求: {}", msg);
                Object result = requestHandler.handle(msg);
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
                } else {
                    logger.error("通道不可写");
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        //});
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

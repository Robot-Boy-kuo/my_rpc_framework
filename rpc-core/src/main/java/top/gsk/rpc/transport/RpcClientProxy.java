package top.gsk.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.entity.RpcRequest;
import top.gsk.rpc.entity.RpcResponse;
import top.gsk.rpc.transport.netty.client.NettyClient;
import top.gsk.rpc.transport.socket.client.SocketClient;
import top.gsk.rpc.util.RpcMessageChecker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author gsk
 * @version 1.0
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
//        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
//        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
//                method.getName(), args, method.getParameterTypes(),false);
//        return client.sendRequest(rpcRequest);
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes(), false);
        RpcResponse rpcResponse = null;
        if (client instanceof NettyClient) {
            //join()和get()方法都是用来获取CompletableFuture异步之后的返回值
            //get()方法值会阻塞主线程，直到子线程执行任务完成返回结果才会取消阻塞。如果子线程一直不返回接口那么主线程就会一直阻塞，所以我们一般不建议直接使用CompletableFuture的get()方法
            CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) client.sendRequest(rpcRequest);
            try {
                //获取sendRequest类型，返回值为rpcResponse
                rpcResponse = completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("方法调用请求发送失败", e);
                return null;
            }
        }
        if (client instanceof SocketClient) {
            rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}

package top.gsk.test;

import top.gsk.rpc.serializer.CommonSerializer;
import top.gsk.rpc.serializer.JasonSerializer;
import top.gsk.rpc.serializer.KryoSerializer;
import top.gsk.rpc.transport.RpcClient;
import top.gsk.rpc.transport.RpcClientProxy;
import top.gsk.rpc.api.HelloObject;
import top.gsk.rpc.api.HelloService;
import top.gsk.rpc.transport.netty.client.NettyClient;

/**
 * @author gsk
 * @version 1.0
 */
public class NettyTestClient {
    public static void main(String[] args) {
        //初始化一个NettyClient客户端，同时新建了NacosServiceRegistry
        //TODO 新建了NacosServiceRegistry的作用以及什么时候能用到
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        //设置客户端的序列化器，调用了RpcClient接口中的抽象方法
        //client.setSerializer(new KryoSerializer());
        //新建一个客户端的代理
        //使用了代理模式来远程调用服务，通过代理去访问
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);

        //获得特定类的代理，相当于对这个代理进行初始化设置
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        //调用的客户端暴露的服务的接口所需要的传入的实参
        HelloObject object = new HelloObject(12,"Hello Server");

        //通过代理调用客户端提供的服务，并获得返回值
        //TODO 此处的调用发生了什么
        String res = helloService.hello(object);

        System.out.println(res);
    }
}

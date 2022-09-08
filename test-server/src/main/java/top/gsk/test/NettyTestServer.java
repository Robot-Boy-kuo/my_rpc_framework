package top.gsk.test;

import top.gsk.rpc.api.HelloService;
import top.gsk.rpc.serializer.CommonSerializer;
import top.gsk.rpc.serializer.JasonSerializer;
import top.gsk.rpc.serializer.KryoSerializer;
import top.gsk.rpc.transport.netty.server.NettyServer;


/**
 * @author gsk
 * @version 1.0
 */
public class NettyTestServer {
    public static void main(String[] args) {
        //初始化helloService，重写了hello方法，准备被调用
        HelloService helloService = new HelloServiceImpl();

        //初始化NettyServer，同时初始化NacosServiceRegistry和ServiceProviderImpl
        //TODO 此处的NacosServiceRegistry和ServiceProviderImpl实现了什么功能，在何时被使用
        NettyServer server = new NettyServer("127.0.0.1",9999, CommonSerializer.KRYO_SERIALIZER);
        //初始化Kryo序列化器
        server.setSerializer(new KryoSerializer());
        //将helloService服务注册到Nacos注册中心
        //publishService为RpcServer接口中定义的抽象方法
        //TODO 此处的publishService方法注册时的全过程
        server.publishService(helloService,HelloService.class);
    }
}

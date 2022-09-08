package top.gsk.test;

import top.gsk.rpc.serializer.KryoSerializer;
import top.gsk.rpc.transport.RpcClientProxy;
import top.gsk.rpc.api.HelloObject;
import top.gsk.rpc.api.HelloService;
import top.gsk.rpc.transport.socket.client.SocketClient;

/**
 * @author gsk
 * @version 1.0
 */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        //client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(13,"Hello Server");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}

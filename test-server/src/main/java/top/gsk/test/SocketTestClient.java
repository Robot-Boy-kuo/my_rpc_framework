package top.gsk.test;

import top.gsk.rpc.api.HelloService;
import top.gsk.rpc.provider.ServiceProviderImpl;
import top.gsk.rpc.provider.ServiceProvider;
import top.gsk.rpc.serializer.KryoSerializer;
import top.gsk.rpc.transport.socket.server.SocketServer;

/**
 * @author gsk
 * @version 1.0
 */
public class SocketTestClient {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.publishService(helloService,HelloService.class);
    }
}

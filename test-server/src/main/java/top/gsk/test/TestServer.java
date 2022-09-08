//package top.gsk.test;
//
//import top.gsk.rpc.api.HelloService;
//import top.gsk.rpc.provider.DefaultServiceRegistry;
//import top.gsk.rpc.provider.ServiceRegistry;
//import top.gsk.rpc.transport.socket.server.RpcServer;
//
///**
// * @author gsk
// * @version 1.0
// */
//public class TestServer {
//    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
//        serviceRegistry.register(helloService);
//        RpcServer rpcServer = new RpcServer(serviceRegistry);
//        rpcServer.start(9000);
//
//
//    }
//}
//
//package top.gsk.test;
//
//import top.gsk.rpc.api.HelloObject;
//import top.gsk.rpc.api.HelloService;
//import top.gsk.rpc.transport.RpcClientProxy;
//
///**
// * @author gsk
// * @version 1.0
// */
//public class TestClient {
//
//    public static void main(String[] args) {
//        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
//        HelloService helloService = proxy.getProxy(HelloService.class);
//        HelloObject object = new HelloObject(13, "Hello, Server");
//        String res = helloService.hello(object);
//        System.out.println(res);
//
//    }
//}

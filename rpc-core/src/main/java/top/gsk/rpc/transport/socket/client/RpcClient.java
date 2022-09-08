package top.gsk.rpc.transport.socket.client;

//import top.gsk.rpc.entity.RpcRequest;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
///**
// * @author gsk
// * @version 1.0
// */
//public class RpcClient {
//    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
//
//    public Object sendRequest(RpcRequest rpcRequest, String host, int port){
//        try (Socket socket = new Socket(host, port)){
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            objectOutputStream.writeObject(rpcRequest);
//            objectOutputStream.flush();
//            return objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            logger.error("调用时发生错误：" ,e);
//            return null;
//        }
//
//    }
//
//}

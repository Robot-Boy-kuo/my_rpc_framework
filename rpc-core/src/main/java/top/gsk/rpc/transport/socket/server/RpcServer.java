//package top.gsk.rpc.transport.socket.server;
//
//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import top.gsk.rpc.handler.RequestHandler;
//import top.gsk.rpc.provider.ServiceRegistry;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.*;
//
//
///**
// * @author gsk
// * @version 1.0
// */
//public class RpcServer {
//
//
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(RpcServer.class);
//
//    private static final int CORE_POOL_SIZE = 5;
//    private static final int MAXIMUM_POOL_SIZE = 50;
//    private static final int KEEP_ALIVE_TIME = 60;
//    private static final int BLOCKING_QUEUE_CAPACITY = 100;
//    private final ExecutorService threadPool;
//    private final ServiceRegistry serviceRegistry;
//    private RequestHandler requestHandler = new RequestHandler();
//
//
//    public RpcServer(ServiceRegistry serviceRegistry) {
//        this.serviceRegistry = serviceRegistry;
//        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
//    }
//
//    public void start(int port){
//        try(ServerSocket serverSocket = new ServerSocket(port)){
//            logger.info("启动服务器，等待客户端连接");
//            Socket socket;
//            while((socket = serverSocket.accept())!=null){
//                logger.info("客户端连接成功，{}:{}", socket.getInetAddress(),socket.getPort());
//                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry));
//            }
//            threadPool.shutdown();
//        }catch (IOException e){
//            logger.error("连接错误：", e);
//        }
//
//    }
//
//}
//
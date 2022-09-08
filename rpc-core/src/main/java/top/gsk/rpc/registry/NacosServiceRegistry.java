package top.gsk.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.enumeration.RpcError;
import top.gsk.rpc.exception.RpcException;
import top.gsk.rpc.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author gsk
 * @version 1.0
 */
public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

//    private static final String SERVER_ADDR = "127.0.0.1:8848";//设置Nacos服务注册中心的地址
//    private static final NamingService namingService;//名字服务需要提供服务的订阅与注册功能
//
//    static {
//        try {
//            namingService = NamingFactory.createNamingService(SERVER_ADDR);
//        } catch (NacosException e) {
//            logger.error("连接到Nacos时有错误发生: ", e);
//            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
//        }
//    }
//
//
//    //针对服务器端进行服务注册 provider  提供服务的一方
//    @Override
//    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
//        try {
//            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());//描述注册一个实例到服务。
//        } catch (NacosException e) {
//            logger.error("注册服务时有错误发生:", e);
//            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
//        }
//    }
//
//    //针对客户端进行服务发现 consumer  调用服务的一方
//    @Override
//    public InetSocketAddress lookupService(String serviceName) {
//        try {
//            List<Instance> instances = namingService.getAllInstances(serviceName);//获取服务下的所有实例，返回参数：List 实例列表
//            Instance instance = instances.get(0);
//            return new InetSocketAddress(instance.getIp(), instance.getPort());
//        } catch (NacosException e) {
//            logger.error("获取服务时有错误发生:", e);
//        }
//        return null;
//    }
@Override
public void register(String serviceName, InetSocketAddress inetSocketAddress) {
    try {
        NacosUtil.registerService(serviceName, inetSocketAddress);
    } catch (NacosException e) {
        logger.error("注册服务时有错误发生:", e);
        throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
    }
}


}

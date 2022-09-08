package top.gsk.rpc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.enumeration.RpcError;
import top.gsk.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author gsk
 * @version 1.0
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    //创建serviceMap用于存放服务器端需要注册的服务，存放类型：ConcurrentHashMap
    //TODO ConcurrentHashMap类型的存放结构
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    //通过ConcurrentHashMap中的方法创建Set类型的结构，用于存放注册的服务名，存放类型是String
    private static final Set<String> resgisterService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized  <T> void addServiceProvider(T service) {
        //获取服务实现类的名称    serviceName= "top.gsk.test.HelloServiceImpl"
        String serviceName = service.getClass().getCanonicalName();
        if(resgisterService.contains(serviceName)){
            return;
        }
        //在Set中加入服务名称
        resgisterService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();//获取服务实现类的实现接口，name = top.gsk.rpc.api.HelloService
        if (interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);//serviceMap中：k：Hello Service v：HelloServiceImpl
        }
        logger.info("向接口：{} 注册服务：{}",interfaces,serviceName);

    }


    @Override
    public synchronized Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

package top.gsk.rpc.provider;

/**
 * @author gsk
 * @version 1.0
 */
public interface ServiceProvider {
    /**
     * @param service 待注册的服务实体
     * @param <T> 服务实体类
     *
     * */
    <T> void addServiceProvider(T service);

    Object getServiceProvider(String serviceName);
}
